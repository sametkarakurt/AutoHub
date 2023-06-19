package com.example.autohub.view


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.autohub.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher2: ActivityResultLauncher<String>
    var selectedPicture : Uri? = null
    var selectedPicture2 : Uri? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()
        registerLauncher2()

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
    }

    fun upload(view: View){
        val uuid = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val imageName2 = "$uuid2.jpg"
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)
        val imageReference2 = reference.child("images").child(imageName2)

        if(selectedPicture != null) {
            imageReference.putFile(selectedPicture!!).addOnSuccessListener{
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    if(selectedPicture2 != null){
                        imageReference2.putFile(selectedPicture2!!).addOnSuccessListener {
                            val uploadPictureReference2 = storage.reference.child("images").child(imageName2)
                            uploadPictureReference2.downloadUrl.addOnSuccessListener {
                                val downloadUrl2 = it.toString()
                                if(auth.currentUser != null){
                                    val postMap = hashMapOf<String,Any>()

                                    postMap.put("downloadUrl",downloadUrl)
                                    postMap.put("downloadUrl2",downloadUrl2)
                                    postMap.put("userEmail",auth.currentUser!!.email!!)
                                    postMap.put("username",auth.currentUser!!.displayName!!)
                                    postMap.put("brand",binding.brandText.text.toString())
                                    postMap.put("accessories",binding.accessoriesTextMultiLine.text.toString())
                                    postMap.put("comment",binding.commentTextMultiLine.text.toString())
                                    postMap.put("date",com.google.firebase.Timestamp.now())

                                    firestore.collection("Posts").add(postMap).addOnSuccessListener {
                                        finish()
                                    }.addOnFailureListener{
                                        Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                                    }

                                }
                            }
                        }
                    }

                }
            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun selectImage(view: View)
    {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()

            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else {
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    fun selectImage2(view: View)
    {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    permissionLauncher2.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()

            } else {
                permissionLauncher2.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else {
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher2.launch(intentToGallery)
        }
    }


    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == RESULT_OK)
            {
                val intentFromResult = result.data
                if(intentFromResult != null)
                {
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let {
                        binding.imageView.setImageURI(it)
                    }
                }
            }

        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if(result){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                Toast.makeText(this@UploadActivity,"Permission needed!",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerLauncher2(){
        activityResultLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == RESULT_OK)
            {
                val intentFromResult = result.data
                if(intentFromResult != null)
                {
                    selectedPicture2 = intentFromResult.data
                    selectedPicture2?.let {
                        binding.imageView2.setImageURI(it)
                    }
                }
            }

        }

        permissionLauncher2 = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if(result){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher2.launch(intentToGallery)
            } else {
                Toast.makeText(this@UploadActivity,"Permission needed!",Toast.LENGTH_LONG).show()
            }
        }
    }


}