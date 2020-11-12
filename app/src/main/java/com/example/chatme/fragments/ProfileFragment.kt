package com.example.chatme.fragments


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.example.chatme.EditProfileActivity
import com.example.chatme.LoginActivity
import com.example.chatme.R
import com.example.chatme.Utils.GridDataClass
import com.example.chatme.Utils.toast
import com.example.chatme.Utils.utilclass
import com.example.chatme.adapters.GridAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var myview: View
    var myname: String? = null
    var myemail: String? = null
    var mypassword: String? = null
    var myimage: String? = null
    var gridlist = ArrayList<GridDataClass>()
    lateinit var gridAdapter: GridAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.total_friend.setOnClickListener {
            if(view.total_friend.text.toString() != "0")
                Navigation.findNavController(view).navigate(R.id.profile_to_myfriends)
        }

        view.change_profile_text.setOnClickListener {
            if(myname.isNullOrEmpty()){
                return@setOnClickListener
            }

            myview = view
            var i = Intent()
            i.setType("image/*")
            i.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(i,"Choose Image"),555)
        }

        view.profile_verify.setOnClickListener {
            FirebaseAuth.getInstance().currentUser!!.sendEmailVerification().addOnCompleteListener {
                if (it.isSuccessful){
                    android.app.AlertDialog.Builder(view.context).apply {
                        setTitle("Check and Verify Your Email, And ReLogin")
                        setPositiveButton("yes"){ _, _ ->
                            FirebaseAuth.getInstance().signOut()
                            var pref = requireContext().getSharedPreferences("chatme", 0)
                            var edit = pref.edit()
                            edit.putString("signout", "true")
                            edit.apply()
                            edit.commit()

                            requireContext().startActivity(Intent(requireActivity(), LoginActivity::class.java))
                            requireActivity().finish()
                        }
                        setNegativeButton("No"){ _, _ -> }
                    }.create().show()
                }
                else{
                    requireContext().toast("Check Email and Verify, And ReLogin")
                    FirebaseAuth.getInstance().signOut()
                    var pref = requireContext().getSharedPreferences("chatme", 0)
                    var edit = pref.edit()
                    edit.putString("signout", "true")
                    edit.apply()
                    edit.commit()

                    requireContext().startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }

        }

        view.toolbar_edit.setOnClickListener {
            if(myname.isNullOrEmpty()){
                return@setOnClickListener
            }
            var intent = Intent(requireContext(),EditProfileActivity::class.java)
            intent.putExtra("name",myname)
            intent.putExtra("email",myemail)
            intent.putExtra("password",mypassword)
            intent.putExtra("image",myimage)
            requireContext().startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 555 && resultCode == Activity.RESULT_OK && data != null){
            var filepath: Uri = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,filepath)
            var imageref: StorageReference = FirebaseStorage.getInstance().reference.child("ProfilePicture").child(FirebaseAuth.getInstance().currentUser!!.uid).child(filepath.lastPathSegment.toString())
            imageref.putFile(filepath).addOnCompleteListener {
                if(it.isSuccessful){
                    imageref.downloadUrl.addOnSuccessListener {url->
                        firebaseDatabase = FirebaseDatabase.getInstance()
                        firebaseDatabase.getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid.toString()).child("image").setValue(url.toString()).addOnCompleteListener {di->

                        }
                    }
                }
                else{
                    requireContext().toast(it.exception!!.message.toString())
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        profile_name.text = ""
        if(!firebaseAuth.currentUser!!.isEmailVerified){
            profile_verify.visibility = View.VISIBLE
        }

        firebaseDatabase.getReference("Users").child(firebaseAuth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                var user = snapshot.value as HashMap<String,String>
                if(user["image"].toString() == "ic_boy"){
                    profile_image.setImageResource(R.drawable.ic_boy)
                }else{
                    Picasso.get().load(user["image"].toString()).into(profile_image as ImageView)
                }
                profile_name.text = user["name"].toString()
                myname = user["name"].toString()
                myemail = user["email"].toString()
                mypassword = user["password"].toString()
                myimage = user["image"].toString()
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message.toString()) }
        })

        firebaseDatabase.getReference("UserManage").child(firebaseAuth.currentUser!!.uid).child("MyFriends").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                total_friend.text = snapshot.childrenCount.toString()
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })

        firebaseDatabase.getReference("UsersPost").orderByChild("id").equalTo(firebaseAuth.currentUser!!.uid.toString()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    total_posts.text = snapshot.childrenCount.toString()
                    for (i in snapshot.children){
                        var img = i.value as HashMap<String,String>
                        var imgdata = GridDataClass(img["postimage"].toString())
                        if(!gridlist.contains(imgdata))
                            gridlist.add(imgdata)
                    }
                    gridAdapter = GridAdapter(gridlist,requireContext())
                    view?.profile_gridview?.adapter = gridAdapter
                    view?.profile_gridview?.setOnItemClickListener { parent, view, position, id ->
                        utilclass.image = gridlist[position].img
                        Navigation.findNavController(view).navigate(R.id.myprofile_to_image)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) { requireContext().toast(error.message) }
        })
    }

}

