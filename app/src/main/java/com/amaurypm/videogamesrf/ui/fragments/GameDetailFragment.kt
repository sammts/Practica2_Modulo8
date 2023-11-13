package com.amaurypm.videogamesrf.ui.fragments

import android.widget.MediaController
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.amaurypm.videogamesrf.application.VideoGamesRFApp
import com.amaurypm.videogamesrf.data.GameRepository
import com.amaurypm.videogamesrf.data.remote.model.GameDetailDto
import com.amaurypm.videogamesrf.databinding.FragmentGameDetailBinding
import com.amaurypm.videogamesrf.util.Constants
import com.google.android.gms.maps.GoogleMap
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.Manifest
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.ui.Map
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private const val GAME_ID = "game_id"


class GameDetailFragment : Fragment(){

    private var gameId: String? = null

    private lateinit var binding1: FragmentGameDetailBinding

    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: GameRepository

    //Para google maps
    private lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding.pbLoading.visibility = View.INVISIBLE

        arguments?.let { args ->
            gameId = args.getString(GAME_ID)

            Log.d(Constants.LOGTAG, "Id recibido: $gameId")

            repository = (requireActivity().application as VideoGamesRFApp).repository

            lifecycleScope.launch {

                gameId?.let { id ->
                    //val call: Call<GameDetailDto> = repository.getGameDetail(id)
                    val call: Call<GameDetailDto> = repository.getGameDetailApiary(id)

                    call.enqueue(object: Callback<GameDetailDto>{
                        override fun onResponse(
                            call: Call<GameDetailDto>,
                            response: Response<GameDetailDto>
                        ) {

                            /*binding.btnOpenU.setOnClickListener {
                                startActivity(Intent(this@GameDetailFragment, Map::class.java))
                            }*/

                            binding.apply {
                                pbLoading.visibility = View.GONE

                                tvTitle.text = response.body()?.name

                                tvAge.text = response.body()?.age

                                tvParent.text = response.body()?.parent

                                tvState.text = response.body()?.state

                                tvLongDesc.text = response.body()?.description

                                //Glide.with(requireContext())
                                    //.load(response.body()?.video)
                                    //.into(ivImage)
                                binding.ivImage.setVideoURI(Uri.parse(response.body()?.video))
                                /*val mc = MediaController(this)
                                mc.setAnchorView(binding.ivImage)
                                binding.ivImage.setMediaController(mc)*/
                                binding.ivImage.start()

                            }

                        }

                        override fun onFailure(call: Call<GameDetailDto>, t: Throwable) {
                            //binding.pbLoading.visibility = View.GONE

                            Toast.makeText(requireActivity(), "No se ha podido establecer una conexión", Toast.LENGTH_SHORT).show()

                            //binding.pbLoading.visibility = View.VISIBLE
                        }

                    })
                }

            }

        }

        //val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(gameId: String) =
            GameDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(GAME_ID, gameId)
                }
            }
    }

    /*override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
    }

    private fun createMarker(){
        val coordinates = LatLng(19.322326,-99.184592)
        val marker = MarkerOptions()
            .position(coordinates)
            .title("Sandra Lopez")
            .snippet("Hermana")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.people))

        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )

    }*/
}