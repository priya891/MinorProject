package com.example.minorproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.type.Date
import kotlinx.android.synthetic.main.add_category_image.*
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark


class AddImageToCategoryFragment : Fragment() {
    private val PICK_IMAGE_REQUEST = 72
    private var filePath: Uri? = null
    private var imageview: ImageView? = null
    private var add: Button?=null
    private var addimage: Button?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var mStorageRef: StorageReference
  var args:String?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.add_category_image, container, false)

        return v

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addimage = view.findViewById<View>(R.id.ac_addimage2) as Button
        args = arguments?.getString("id")
        imageview = view.findViewById<View>(R.id.ac_image2) as ImageView
        addimage?.setOnClickListener { launchGallery() }
        add_category_image_button.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            mStorageRef = FirebaseStorage.getInstance().getReference()
            if(filePath != null){
                val ref = mStorageRef.child("category detail image/" +args )

                val uploadTask = ref.putFile(filePath!!)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user: MutableMap<String,Any> = HashMap()

                        val downloadUri = task.result
                        val uri=downloadUri.toString()
                        val date = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        val formatted = date.format(formatter)

                        user["Date"]=formatted

                        user["imageUrl"] = uri

                        val db = FirebaseFirestore.getInstance()
                        db.collection("category image").document(args!!).collection("category image details").add(user)
                                .addOnSuccessListener{DocumentReference->
                                    val id=DocumentReference.id

                                                db.collection("timeLine image").document(auth.currentUser!!.uid).collection("timeline").document(id).set(user, SetOptions.merge()).addOnSuccessListener {

                                                    val categorydetails: Fragment = CategoryDetail()
                                                    Log.d("id", "${args}")
                                                    val bundle = Bundle()
                                                    bundle.putString("id", args)
                                                    categorydetails.arguments = bundle
                                                    (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_container, categorydetails).addToBackStack("frag6").commit()

                                                }






            }}}}

        }
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data

        }
    }


}