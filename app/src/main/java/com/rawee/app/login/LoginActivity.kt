package com.rawee.app.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.rawee.app.MainActivity
import com.rawee.app.R
import com.rawee.app.RaweeApplication
import com.rawee.app.utils.Logger
import com.rawee.app.utils.firebase.FireDataManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), FacebookCallback<LoginResult> {

    private val TAG: String = javaClass.simpleName

    private val RC_SIGN_IN: Int = 9001

    private val mGoogleApiHelper: GoogleApiHelper = RaweeApplication.getGoogleApiHelper()//Google
    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()//Firebase
    private val mCallbackManager: CallbackManager = CallbackManager.Factory.create() //FB
    private val fireDataManager: FireDataManager = FireDataManager() //DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        changeStatusBarColour(Color.parseColor("#000000"))

        val fbIconScale = 1.45F
        val drawable = this.resources.getDrawable(
                R.drawable.com_facebook_button_icon)
        drawable.setBounds(0, 0, ((drawable.intrinsicWidth * fbIconScale).toInt()),
                ((drawable.intrinsicHeight * fbIconScale).toInt()))
        btFacebookLogin.setCompoundDrawables(drawable, null, null, null)
        btFacebookLogin.compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.fb_margin_override_textpadding)
        btFacebookLogin.setPadding(
                resources.getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                resources.getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                resources.getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                resources.getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));

        // Initialize Facebook Login button
        btFacebookLogin.setReadPermissions("email", "public_profile")
        //btFacebookLogin.setPadding(32, 32, 10, 30)
        btFacebookLogin.registerCallback(mCallbackManager, this)

        //Google
        btGoogleLogin.setColorScheme(SignInButton.COLOR_DARK)
        btGoogleLogin.setSize(SignInButton.SIZE_STANDARD)

        btGoogleLogin.setOnClickListener { view -> onGoogleSignInButtonClicked(view) }
        btFacebookLogin.setOnClickListener { view -> onFacebookSignInButtonClicked(view) }
        btEmailLogin.setOnClickListener { view -> onEmailLoginClicked(view) }
    }

    private fun onEmailLoginClicked(view: View?) {
        val email = etEmail.text.toString()
        if (!EmailAuth.isEmailValid(email)) {
            //error
            return
        }
        mFirebaseAuth.sendSignInLinkToEmail(email, EmailAuth.getEmailFormat())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        //Yoo
                    } else {

                    }
                }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        processStatus(mFirebaseAuth.currentUser)
    }

    private fun processStatus(user: FirebaseUser?) {
        if (user != null) {
            //Signed in -> Proceed to BaseMainActivity.
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
        }
    }

    private fun showProgressDialog() {
        progressbarLogin.visibility = View.VISIBLE
    }

    private fun hideProgressDialog() {
        progressbarLogin.visibility = View.GONE
    }

    private fun onGoogleSignInButtonClicked(view: View) {
        signInGoogle()
    }

    private fun onFacebookSignInButtonClicked(view: View) {
        showProgressDialog()
    }

    private fun signInGoogle() {
        showProgressDialog()
        val signInIntent = mGoogleApiHelper.googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        showProgressDialog()
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user: FirebaseUser? = mFirebaseAuth.currentUser
                if (user != null) {
                    launchMainActivity(user)
                } else {
                    // TODO: Login fail
                }
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
            }
            hideProgressDialog()
            // ...
        })
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        showProgressDialog()
        val credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user: FirebaseUser? = mFirebaseAuth.currentUser
                        if (user != null) {
                            launchMainActivity(user)
                        } else {
                            // TODO: Login fail
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Logger.getsInstance().logW(TAG, "signInWithCredential:failure", task.getException())
                        //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        processStatus(null)
                    }
                    hideProgressDialog()
                });
    }

    private fun launchMainActivity(user: FirebaseUser?) {
        if (user == null) return
        hideProgressDialog()
        //save user
        fireDataManager.saveUser(user)
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
    }

    override fun onSuccess(result: LoginResult) {
        handleFacebookAccessToken(result.accessToken)
    }

    override fun onCancel() {
        hideProgressDialog()
        Toast.makeText(applicationContext, "SignIn canceled", Toast.LENGTH_SHORT).show()
    }

    override fun onError(error: FacebookException?) {
        hideProgressDialog()
        Toast.makeText(applicationContext, "Fail to SignIn with facebook.\nPoor or No internet connection.", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_SIGN_IN -> {
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        if (task.isSuccessful) {
                            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                            firebaseAuthWithGoogle(account)
                        } else {
                            hideProgressDialog()
                            Toast.makeText(applicationContext, "Google SignIn failed.\nPoor or No Internet connection.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (ex: ApiException) {
                        Logger.getsInstance().logW(TAG, "Google Sign in failed", ex)
                        hideProgressDialog()
                        Toast.makeText(applicationContext, "Google SignIn failed.\nPoor or No Internet connection.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun changeStatusBarColour(parseColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.statusBarColor = parseColor
        }
    }
}