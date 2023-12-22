package com.example.uas_papb.User

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.uas_papb.MainActivity
import com.example.uas_papb.PrefManager
import com.example.uas_papb.R
import com.example.uas_papb.data_class.Favorite
import com.example.uas_papb.data_class.Movie
import com.example.uas_papb.databinding.ActivityDetailsBinding
import com.example.uas_papb.room.FavoriteDao
import com.example.uas_papb.room.FilmRoom
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailsActivity : AppCompatActivity() {
    private var idMovie = ""
    private val binding by lazy {
        ActivityDetailsBinding.inflate(layoutInflater)
    }
    private val firestore = FirebaseFirestore.getInstance()
    private val movies = firestore.collection("movies")


    private lateinit var executorService: ExecutorService
    private lateinit var mFavoriteDao: FavoriteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = FilmRoom.getDatabase(this)
        mFavoriteDao = db!!.FavoriteDao()

        val args: DetailsActivityArgs by navArgs()
        idMovie = args.idMovie

        val prefManager = PrefManager.getInstance(this)

        movies.document(idMovie).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists() && snapshot != null) {
                val movieData = snapshot.toObject(Movie::class.java)!!
                with(binding) {
                    movieTitle.setText(movieData.judul)
                    movieDescription.setText(movieData.deskripsi)

                    // Load gambar dari alamat URL yang sudah ada di Firestore
                    Glide.with(this@DetailsActivity)
                        .load(movieData.gambar)
                        .into(imageFilm)

                    buttonFav.setOnClickListener{
                        val usn = prefManager.getUsername()
                        val newFav = Favorite(judul = movieData.judul, gambar = movieData.gambar, username = usn)
                        addFavorite(newFav)
                        notif(movieData)
                    }
                }
            }
        }
    }

    fun addFavorite (favorite: Favorite){
        executorService.execute{
            mFavoriteDao.Insert(favorite)
        }
    }
    fun notif(movie: Movie) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "Order_Success"
        val channelName = "Order Success Channel"

        // Membuat saluran notifikasi jika belum ada
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Intent untuk membuka DetailsActivity setelah notifikasi diklik
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Membuat notifikasi
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Penambahan Film Favorite")
            .setContentText("Film ${movie.judul} berhasil ditambahkan ke menu favorite.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Menampilkan notifikasi
        val notificationId = 123 // Ganti dengan identifier unik sesuai kebutuhan
        notificationManager.notify(notificationId, notificationBuilder.build())
    }


}