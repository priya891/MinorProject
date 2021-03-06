package com.example.minorproject.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.minorproject.R
import com.example.minorproject.databinding.ListItemCategorydetailBinding
import com.example.minorproject.model.AddCategoryImageModelClass

class CategoryDetailAdapter(var context: Context, var arrayList: ArrayList<AddCategoryImageModelClass>): RecyclerView.Adapter<CategoryDetailAdapter.ItemHolder >() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ItemHolder {
        val inflater= LayoutInflater.from(parent.context)
        val  binding:ListItemCategorydetailBinding = DataBindingUtil.inflate(inflater, R.layout.list_item_categorydetail, parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    override fun onBindViewHolder(holder:ItemHolder, position: Int) {
       holder.binding.addCategoryImageModelClass=arrayList.get(position)
        holder.binding.executePendingBindings()


        val alphaChar: AddCategoryImageModelClass = arrayList.get(position)

            Glide.with(context)
                    .load(alphaChar.iconsChar)
                    .into(holder.binding.image2)

        val fiebaseid_CategoryDetails = alphaChar.id
        val firebaseId_Category = alphaChar.args
            holder.binding.image2.setOnClickListener {
                val FullScreenImage: Fragment = FullScreenImageFragment()
                val bundle = Bundle()
                bundle.putString("id2", fiebaseid_CategoryDetails)
                bundle.putString("id1", firebaseId_Category)
                FullScreenImage.arguments = bundle
                (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_container, FullScreenImage).addToBackStack("frag5").commit()

            }
        }

   class ItemHolder(var binding: ListItemCategorydetailBinding) : RecyclerView.ViewHolder(binding.root) {
   }

    }


