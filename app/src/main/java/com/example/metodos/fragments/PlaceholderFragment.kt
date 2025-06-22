package com.example.metodos.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.metodos.R

// Un fragmento genérico para usar en las pestañas que aún no hemos desarrollado
class PlaceholderFragment : Fragment() {
    private var pageTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageTitle = it.getString(ARG_PAGE_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_placeholder, container, false)
        val textView: TextView = view.findViewById(R.id.placeholder_text)
        textView.text = "Sección de $pageTitle en desarrollo."
        return view
    }

    companion object {
        private const val ARG_PAGE_TITLE = "page_title"
        @JvmStatic
        fun newInstance(pageTitle: String) =
            PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PAGE_TITLE, pageTitle)
                }
            }
    }
}
