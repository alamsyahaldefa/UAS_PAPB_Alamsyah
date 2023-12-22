package com.example.uas_papb.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uas_papb.R
import com.example.uas_papb.data_class.Movie
import com.google.firebase.firestore.FirebaseFirestore

class AddMovieActivity : AppCompatActivity() {

    private lateinit var imageViewMovie: ImageView
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText

    private val PICK_IMAGE_REQUEST = 2
    private val firestore = FirebaseFirestore.getInstance()
    private val movies = firestore.collection("movies")

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        val textViewTitle: TextView = findViewById(R.id.textViewTitle)
        imageViewMovie = findViewById(R.id.imageViewMovie)
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
        val buttonAdd: Button = findViewById(R.id.buttonAdd)

        textViewTitle.text = "Add Movie"

        imageViewMovie.setOnClickListener {
            openGallery()
        }

        buttonAdd.setOnClickListener {
            if (selectedImageUri != null) {
                val newMovie = Movie(
                    gambar = selectedImageUri.toString(),
                    judul = editTextTitle.text.toString(),
                    deskripsi = editTextDescription.text.toString()
                )
                // Simpan ke Firestore atau lakukan tindakan lain yang Anda butuhkan
                 movies.add(newMovie).addOnSuccessListener { docRef->
                     newMovie.id = docRef.id
                     docRef.set(newMovie)
                     Toast.makeText(this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                     finish()
                 }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            imageViewMovie.setImageURI(selectedImageUri)
        }
    }
}
