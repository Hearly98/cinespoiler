package com.cinespoiler.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.cinespoiler.R

class MensajeExitosCompraFragment : DialogFragment() {
    private lateinit var tvTitleMessage: TextView
    private lateinit var tvUserId: TextView
    private lateinit var tvMovieName: TextView
    private lateinit var tvFunctionDate: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvButacas: TextView
    private lateinit var btnOk: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mensaje_exitos_compra, container, false)

         tvTitleMessage = view.findViewById(R.id.tv_title_message)
         tvUserId = view.findViewById(R.id.tv_user_id)
         tvMovieName = view.findViewById(R.id.tv_movie_name)
         tvFunctionDate = view.findViewById(R.id.tv_function_date)
         tvTotal = view.findViewById(R.id.tv_total)
         tvButacas = view.findViewById(R.id.tv_butacas)
         btnOk = view.findViewById(R.id.btn_ok)

        val userId = arguments?.getString("userId") ?: "N/A"
        val movieName = arguments?.getString("movieName") ?: "N/A"
        val functionDate = arguments?.getString("functionDate") ?: "N/A"
        val totalAmount = arguments?.getDouble("totalAmount") ?: 0.0
        val seats = arguments?.getString("seats") ?: "N/A"

        tvUserId.text = "Usuario: $userId"
        tvMovieName.text = "Nombre de la Película: $movieName"
        tvFunctionDate.text = "Fecha de Función: $functionDate"
        tvTotal.text = "Monto Total: S/. %.2f".format(totalAmount)
        tvButacas.text = "Butacas: $seats"

        btnOk.setOnClickListener {
            dismiss()
        }

        return view
        }

companion object {
    fun newInstance(userId: String, movieName: String, functionDate: String, totalAmount: Double, seats: String): MensajeExitosCompraFragment {
        val fragment = MensajeExitosCompraFragment()
        val args = Bundle()
        args.putString("userId", userId)
        args.putString("movieName", movieName)
        args.putString("functionDate", functionDate)
        args.putDouble("totalAmount", totalAmount)
        args.putString("seats", seats)
        fragment.arguments = args
        return fragment
    }
}
}