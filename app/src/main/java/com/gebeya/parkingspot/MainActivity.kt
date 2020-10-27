package com.gebeya.parkingspot


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    lateinit var callbackManager: CallbackManager
    private val EMAIL = "email"

    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.gebeya.parkingspot.R.layout.activity_main)

        //google signin
        val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        google_ic.setOnClickListener {
            signIn()
        }

        //facebook signin


        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        Toast.makeText(
                                this@MainActivity,
                                "Facebook login successful",
                                Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)

                    }

                    override fun onCancel() {
                        Toast.makeText(this@MainActivity, "Facebook login Cancelled", Toast.LENGTH_LONG)
                                .show()
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                    }
                })

        facebook_ic.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                    this@MainActivity, Arrays.asList("public_profile", "user_friends")
            )
        }
        retrofitInterface = retrofit!!.create(MyService::class.java)

        login_btn.setOnClickListener {
            if (email.text.toString().trim().isEmpty()) {
                email.error = "Email Required"
                email.requestFocus()
                return@setOnClickListener
            }
            if (password.text.toString().trim().isEmpty()) {
                password.error = "Password Required"
                password.requestFocus()
                return@setOnClickListener
            }
            val map = HashMap<String, String>()

            map["email"] = email.text.toString()
            map["password"] = password.text.toString()

            val call = retrofitInterface!!.executeLogin(map)
            sessionManager= SessionManager(this)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                ) {
                    if (response.code()==200 && response.body()!=null) {

                        sessionManager.saveAuthToken(response.body()!!.token)
                        Toast.makeText(this@MainActivity,"User Logged in successful ${response.body()!!.token}", Toast.LENGTH_LONG).show()
                        //toasted the token to check if its working.

                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else if (response.code() == 400) {

                        Toast.makeText(this@MainActivity, "User doesnt exist ", Toast.LENGTH_LONG)
                                .show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
        registration_link.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
        forgotpw.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
                signInIntent, RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)

        } catch (e: ApiException) {
            Log.e("failed code=", e.statusCode.toString())
        }

    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)

    }
}
