package com.example.f_databaseworkmanager

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.f_databaseworkmanager.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var firebaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        firebaseRef = FirebaseDatabase.getInstance().getReference("UserDatabase")

        binding.savebtn.setOnClickListener {


            val uid = firebaseRef.push().key!!

            val name = binding.name.text.toString()
            val city = binding.city.text.toString()

            val user = User(uid, name, city)
            firebaseRef.child(uid).setValue(user).addOnSuccessListener {
                Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_LONG).show()
            }

            binding.name.text.clear()
            binding.city.text.clear()
        }

        binding.readbtn.setOnClickListener {
            firebaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()){
                        val userlist: ArrayList<User> = arrayListOf()
                        for(userDataSnap in dataSnapshot.children){
                            val userData = userDataSnap.getValue(User::class.java)


                            userlist.add(userData!!)
                            binding.textView.text = userlist.toString()
                            Log.d("TAG Snapshot" ,"Value $userDataSnap")
                            Log.d("TAG Userdata" ,"Value $userDataSnap")
                            Log.d("TAG DataSnapshot","DataSnap $userlist")
                        }
//                        val value = dataSnapshot.getValue(User::class.java)
//                        Log.d("TAF Data","Value $")
//                        binding.textView.text = value.toString()


                    }
//
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })


        }


        
    }
}