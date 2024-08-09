package com.example.betapp.Activity

import RetrofitInstance
import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.fragment.AboutusFragment
import com.example.betapp.fragment.Change_pass_Fragment
import com.example.betapp.fragment.DepositHistoryFragment
import com.example.betapp.fragment.HomeFragment
import com.example.betapp.fragment.NoticeFragment
import com.example.betapp.fragment.RateChartFragment
import com.example.betapp.fragment.TransactiondetailFragment
import com.example.betapp.fragment.WinDataFragment
import com.example.betapp.fragment.WithdrawRequestFragment
import com.example.betapp.fragment.bid_historyFragment
import com.example.betapp.fragment.profileFragment
import com.example.betapp.fragment.withdrawHistfragment
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.misc.ToolbarChangeListener
import com.example.betapp.model.appversion
import com.example.betapp.model.user
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener,ToolbarChangeListener {
    private lateinit var  drawerLayout:DrawerLayout;
    private lateinit var toolbar:Toolbar;
    private lateinit var username:TextView
    private lateinit var mobile:TextView
    private lateinit var wallet:TextView
    private val CHANNEL_ID = "notification_channel"
    private val NOTIFICATION_ID = 1
    private lateinit var reload:ImageView
    private lateinit var whatsapp:TextView
    private lateinit var user: user
    private lateinit var commonSharedPrefernces:CommonSharedPrefernces
    private var rotateAnimator: ObjectAnimator? = null
    private val rotationDuration = 500L
    private fun getFCMRegistrationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // The registration token
                val token = task.result
                Log.d("FCM Token", token ?: "Token is null")

                // Now you can send this token to your server
            } else {
                Log.e("FCM Token", "Error getting token: ${task.exception}")
            }
        }
    }
    private  var autodeposit:Boolean=false;
private  var whartsapp:String=""
    val callappversion=object :Callback<appversion>{
        override fun onResponse(call: Call<appversion>, response: Response<appversion>) {
            val pInfo = packageManager.getPackageInfo(
                packageName, 0
            )
            val version = pInfo.versionName
            if(response.isSuccessful){
                if(!response.body()!!.version.equals(version)){
                    startActivity(Intent(this@MainActivity,
                        com.example.betapp.Activity.Appversioncheck::class.java))
                    finish()
                }
            }
        }

        override fun onFailure(call: Call<appversion>, t: Throwable) {

        }

    }
    private fun checkversion(){
       RetrofitInstance.instance.app_version().enqueue(callappversion)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this);
        checkversion()
        FirebaseMessaging.getInstance().subscribeToTopic("your_topic_name")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM Topic", "Successfully subscribed to topic")
                } else {
                    Log.e("FCM Topic", "Error subscribing to topic: ${task.exception}")
                }
            }

        getFCMRegistrationToken()
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar)
        drawerLayout=findViewById(R.id.drawerlayout);
        createNotificationChannel()
        val apiCall:ApiCall= ApiCall()
        apiCall.autodeposit(object :ApiCall.WebseiteSetting{
            override fun onWebsiteSettingReceived(wh: String) {
                if(wh.equals("working"))
                      autodeposit=true
                else autodeposit=false
            }

            override fun onFailure(error: String) {

            }

        })

        apiCall.websiteSetting(object :ApiCall.WebseiteSetting{
            override fun onWebsiteSettingReceived(wh: String) {
                whartsapp=wh
            }

            override fun onFailure(error: String) {

            }

        })
        // Check and request notification permissions
        if (!areNotificationsEnabled()) {
            showNotificationPermissionDialog()
        } else {
            // Notifications are already enabled
            // You can proceed with sending notifications or other actions
        }
        val navigationView:NavigationView=findViewById(R.id.nav_view);
        commonSharedPrefernces =CommonSharedPrefernces(this)
         user= commonSharedPrefernces.getuser()!!;
        val Header=navigationView.getHeaderView(0)

        username=Header.findViewById(R.id.user_name);
        mobile=Header.findViewById(R.id.user_mobile);

        if (user != null) {
            username.setText(user.name)
            mobile.setText(user.mobile)
        }
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.itemIconTintList=null
        val toggle:ActionBarDrawerToggle=ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle);
        val title:TextView=findViewById(R.id.toolbarTitle)
        title.setText(user.email)

        toggle.syncState();
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()
        navigationView.setCheckedItem(R.id.nav_home)
        if(savedInstanceState!=null){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }
        wallet=findViewById(R.id.wallet_balanceToolbar)
        reload=findViewById(R.id.refresh)
        setwallet()
        setupRotateAnimation()

        // Set up a handler to stop the rotation after 3 seconds
        Handler().postDelayed({
            stopRotateAnimation()
        }, rotationDuration)
        reload.setOnClickListener {
            if (rotateAnimator?.isRunning == true) {
                stopRotateAnimation()
            } else {
                startRotateAnimation()
            }
            setwallet()
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "wallet") {
                  setwallet()
                }
            }
        }, IntentFilter("wallet"))

    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "My Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun areNotificationsEnabled(): Boolean {
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        return notificationManagerCompat.areNotificationsEnabled()
    }
    private fun showNotificationPermissionDialog() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle("Notification Permission Required")
            .setContentText("Please enable notifications to receive updates.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }
    private fun setupRotateAnimation() {
        // Create an ObjectAnimator to rotate the ImageView
        rotateAnimator = ObjectAnimator.ofFloat(reload, "rotation", 0f, 360f)
        rotateAnimator?.duration = rotationDuration
        rotateAnimator?.interpolator = LinearInterpolator()

        // Start the animation
        startRotateAnimation()
    }
    public fun setwallet(){
       val ApiCall=ApiCall()
        Log.d("userId",user.id)
        ApiCall.walletApi(user!!.id,object :ApiResponse{
            override fun onSuccess(jsonObject: JsonObject) {
                if(jsonObject.get("status").toString().equals("\"success\"")){
                    val b=jsonObject.get("walletBalance").toString().removeSurrounding("\"")
                    wallet.setText(b)
                }
                else
                    wallet.setText("0")
            }

            override fun onFailure(failure: String) {
          wallet.setText("0")
            }

        })
    }
    private fun startRotateAnimation() {
        rotateAnimator?.start()
    }

    private fun stopRotateAnimation() {
        rotateAnimator?.cancel()
    }

    private fun showCustomDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // Set the title and message for the dialog
        alertDialogBuilder.setTitle("Confirmation")
        alertDialogBuilder.setMessage("Do you want to Logout?")

        // Set the positive button and its action
        alertDialogBuilder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            // Do something when the user clicks "Yes"
            // For example, you can perform an action or dismiss the dialog
            commonSharedPrefernces.logout()
            startActivity(Intent(
                this@MainActivity,
                Login_Activity::class.java
            ))
            finish()
            dialogInterface.dismiss()
        }

        // Set the negative button and its action
        alertDialogBuilder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            // Do something when the user clicks "No"
            // For example, you can perform an action or dismiss the dialog
            dialogInterface.dismiss()
        }

        // Create and show the dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
    R.id.nav_home-> {supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()
    }
    R.id.my_profile->supportFragmentManager.beginTransaction().replace(R.id.fragment_container,profileFragment()).commit()
    R.id.nav_wallet_history-> {if(autodeposit){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            com.example.betapp.fragment.walletFragment2()
        ).commit()}
    else{
        sendtowhatsapp()
    }
    }
    R.id.Notice->supportFragmentManager.beginTransaction().replace(R.id.fragment_container,NoticeFragment()).commit()
    R.id.nav_bidHistory-> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,bid_historyFragment()).commit()
    R.id.nav_Transaction_Details->supportFragmentManager.beginTransaction().replace(R.id.fragment_container,TransactiondetailFragment()).commit()
    R.id.nav_gameRate-> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,RateChartFragment()).commit()
    R.id.nav_withdrawFunds->{
        Log.d("autodeposit",autodeposit.toString())
        if(autodeposit){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,WithdrawRequestFragment()).commit()}
    else{
        sendtowhatsapp()
    }}
    R.id.nav_aboutUs-> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,AboutusFragment()).commit()
    R.id.nav_share->{
        showShareDialog()
    }
    R.id.nav_changePassword-> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,Change_pass_Fragment()).commit()
    R.id.nav_logout->{
       showCustomDialog()
    }
            R.id.navwin_Transaction_Details->supportFragmentManager.beginTransaction().replace(R.id.fragment_container,WinDataFragment()).commit()
            R.id.navwith_Transaction_Details->supportFragmentManager.beginTransaction().replace(R.id.fragment_container,withdrawHistfragment()).commit()
            R.id.navdeposit_Transaction_Details->supportFragmentManager.beginTransaction().replace(R.id.fragment_container,DepositHistoryFragment()).commit()

}
        closeDrawerSmoothly()


      //  drawerLayout.closeDrawer(GravityCompat.START)
        return  true;
    }

    private fun sendtowhatsapp() {
        val i = Intent(Intent.ACTION_VIEW)
        try {
            val url =
                "https://api.whatsapp.com/send?phone=" + "$whartsapp" + "&text=I want to deposit/withdraw manually"
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        setwallet()
    }
    private fun showShareDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Share App")
            .setMessage("Do you want to share this app?")
            .setPositiveButton("Yes") { dialog, which ->
                shareApp()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Download app from https://marutibets.com")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
    private  val DOUBLE_PRESS_INTERVAL = 2000
    private var lastBackPressedTime: Long = 0
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastBackPressedTime < DOUBLE_PRESS_INTERVAL) {
                // Perform the action you want when double pressed
                super.onBackPressed()
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
                lastBackPressedTime = currentTime
            }
        }
    }


    private fun closeDrawerSmoothly() {
        val animator = ObjectAnimator.ofFloat(drawerLayout, View.TRANSLATION_X, 0f)
        animator.duration = 300
      animator.addListener(object : AnimatorListenerAdapter(){
          override fun onAnimationEnd(animation: Animator) {
              drawerLayout.closeDrawer(GravityCompat.START)
          }
      })
        animator.start();
    }
    override fun onToolbarTitleChange(newTitle: String) {
        supportActionBar?.title = newTitle
    }

}


