package com.cinespoiler

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

import com.bumptech.glide.Glide
import com.cinespoiler.adapter.MovieAdapter
import com.cinespoiler.model.Movie
import com.cinespoiler.ui.fragments.MovieDetailsFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EntradasFragment : Fragment() {
    private lateinit var tvButacaTotal: TextView
    private lateinit var movieName: TextView
    private lateinit var moviePrice: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var fechaCompra: EditText
    private var seleccionarButacas = mutableListOf<String>()
    private var totalPrice = 0.0
    private var butacaPrice = 0.0
    private lateinit var btnButaca: List<ImageButton>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_entradas, container, false)
        movieName = view.findViewById(R.id.tvMovieName_Butaca)
        tvTotalPrice = view.findViewById(R.id.tvTotal)
        fechaCompra = view.findViewById(R.id.etFechaCompra)
        tvButacaTotal = view.findViewById(R.id.tvButacaTotal)
        moviePrice = view.findViewById(R.id.tvMoviePriceButaca)
         btnButaca = listOf(
                view.findViewById(R.id.A1),
                view.findViewById(R.id.A2),
                view.findViewById(R.id.A3),
                view.findViewById(R.id.A4),
                view.findViewById(R.id.B1),
                view.findViewById(R.id.B2),
                view.findViewById(R.id.B3),
                view.findViewById(R.id.B4),
                view.findViewById(R.id.B5),
                view.findViewById(R.id.B6),
                view.findViewById(R.id.C1),
                view.findViewById(R.id.C2),
                view.findViewById(R.id.C3),
                view.findViewById(R.id.C4),
                view.findViewById(R.id.C5),
                view.findViewById(R.id.C6)
            )

        val movieId = arguments?.getString("movieId")?:return view
        loadMovieDetails(movieId)
        fechaCompra.setOnClickListener { mostrarDatePicker() }

        setupAsientoButacas(view)
        return view
    }

    private fun setupAsientoButacas(view: View?) {
        for(button in btnButaca){
            button?.setOnClickListener {
                toggleSeleccionarButacas(button)
            }
        }
    }

    private fun toggleSeleccionarButacas(button: ImageButton) {
        val butacasId = button.id.toString()
        if(seleccionarButacas.contains(butacasId)){
            seleccionarButacas.remove(butacasId)
            totalPrice -= butacaPrice
        }else {
            seleccionarButacas.add(butacasId)
            totalPrice += butacaPrice
        }
        tvTotalPrice.text = getString(R.string.total_price_format, totalPrice)
        tvButacaTotal.text = seleccionarButacas.joinToString(", ")
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val date = Calendar.getInstance()
                date.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                fechaCompra.setText(dateFormat.format(date.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = arguments?.getString("movieId")?: return
        loadMovieDetails(movieId)
    }

    private fun loadMovieDetails(movieId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("movies").document(movieId).get()
            .addOnSuccessListener {
                    document ->
                if(document != null ){
                    val name = document.getString("movieName")
                    val price = document.getDouble("moviePrice")?: 0.0

                    movieName.text = name
                    butacaPrice = price
                }
            }.addOnFailureListener{
                Log.e("Cargar detalle pelicula", "Ha ocurrido un error al obterner los datoos")
            }
    }
    companion object{
        fun newInstance(movieId: String): EntradasFragment {
            val fragment = EntradasFragment()
            val args= Bundle()
            args.putString("movieId", movieId)
            fragment.arguments = args
            return fragment
        }
    }
}