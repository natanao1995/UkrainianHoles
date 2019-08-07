package com.example.ukrainianholes.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.ResultSuccess
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObserve()

        viewModel.getLastWins()
    }

    private fun setupObserve() {
        viewModel.lastWinsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultSuccess -> {
                    (recyclerLastWins.adapter as? LastWinsRecyclerAdapter)?.setItems(it.data)
                }
            }
        })
    }

    private fun setupUi() {
        buttonAddHole.setOnClickListener { view ->
            view.findNavController().navigate(R.id.directionAddHole)
        }
        recyclerLastWins.adapter = LastWinsRecyclerAdapter()
    }
}
