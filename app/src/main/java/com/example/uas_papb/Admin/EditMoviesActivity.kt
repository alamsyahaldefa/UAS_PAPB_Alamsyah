package com.example.uas_papb.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.uas_papb.data_class.Movie
import com.example.uas_papb.databinding.ActivityEditMoviesBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditMoviesActivity : AppCompatActivity() {
    private var idMovie = ""
    private val binding by lazy {
        ActivityEditMoviesBinding.inflate(layoutInflater)
    }
    private val PICK_IMAGE_REQUEST = 2
    private val firestore = FirebaseFirestore.getInstance()
    private val movies = firestore.collection("movies")

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        idMovie = intent.getStringExtra("MOVIE_ID").toString()

        if (idMovie == "") { // mode tambah movie
            with(binding) {
                textViewTitle.text = "Add Movie"
                buttonDelete.visibility = View.GONE

                imageViewMovie.setOnClickListener {
                    openGallery()
                }

                buttonSubmit.setOnClickListener {
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
                            Toast.makeText(this@EditMoviesActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }
        }
        else { // mode edit
            binding.textViewTitle.text = "Edit Movie"
            movies.document(idMovie).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists() && snapshot != null) {
                    val movieData = snapshot.toObject(Movie::class.java)!!
                    with(binding) {
                        textViewTitle.text = "Edit Movie"
                        editTextTitle.setText(movieData.judul)
                        editTextDescription.setText(movieData.deskripsi)

                        // Load gambar dari alamat URL yang sudah ada di Firestore
                        Glide.with(this@EditMoviesActivity)
                            .load(movieData.gambar)
                            .into(imageViewMovie)

                        imageViewMovie.setOnClickListener {
                            openGallery()
                        }

                        buttonSubmit.setOnClickListener {
                            if (selectedImageUri != null) {
                                // Update data film di Firestore
                                movieData.judul = editTextTitle.text.toString()
                                movieData.deskripsi = editTextDescription.text.toString()
                                movieData.gambar = selectedImageUri.toString()

                                movies.document(idMovie).set(movieData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this@EditMoviesActivity,
                                            "Data Berhasil Diupdate",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    }
                            }
                        }
                        buttonDelete.setOnClickListener {
                            deleteMovie()
                        }
                    }
                }
            }
        }
    }

    private fun deleteMovie() {
        // Hapus film dari Firestore
        movies.document(idMovie).delete()
            .addOnSuccessListener {
                Toast.makeText(
                    this@EditMoviesActivity,
                    "Film Berhasil Dihapus",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@EditMoviesActivity,
                    "Gagal Menghapus Film",
                    Toast.LENGTH_SHORT
                ).show()
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
            binding.imageViewMovie.setImageURI(selectedImageUri)
        }
    }
}