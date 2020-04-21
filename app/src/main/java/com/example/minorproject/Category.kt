package com.example.minorproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minorproject.model.AddCategoryModelClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Category : Fragment() {
    var recyclerView:RecyclerView?=null
    var f:FloatingActionButton?=null
    var gridLayoutManager: GridLayoutManager?=null
    var arrayList:ArrayList<AddCategoryModelClass>?=null
    var AlphaAdapter:AddCategoryAdapter?=null
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v=inflater.inflate(R.layout.categorylayout, container, false)
        recyclerView = v?.findViewById(R.id.rv)
        gridLayoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        auth = FirebaseAuth.getInstance()

            var item: ArrayList<AddCategoryModelClass> = ArrayList()
            val db = FirebaseFirestore.getInstance()
            db.collection("category").document(auth.currentUser!!.uid).collection("category details").get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                var id = document.id

                                var imageUrl = document.data.get("imageUrl").toString()
                                var title = document.data.get("Title").toString()
                                item.add(AddCategoryModelClass(imageUrl, title,id))
                                AlphaAdapter=AddCategoryAdapter(context!!,item!!)
                                recyclerView?.adapter=AlphaAdapter
                            }
                        }
                    }
        return v

    }

}
