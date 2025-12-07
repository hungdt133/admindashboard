package com.coffeeshop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.coffeeshop.databinding.FragmentOrdersBinding
import com.coffeeshop.models.Order
import com.coffeeshop.adapters.OrderAdapter

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sample data
        val sampleOrders = listOf(
            Order(
                "#95EF16",
                "23:30 05/12/2025",
                "Nguyễn Văn A (Quận tư viễn)",
                "0901234567",
                "120.000 đ",
                "Cancelled"
            ),
            Order(
                "#95EDC6",
                "21:53 05/12/2025",
                "Nguyễn Văn A (Quận tư viễn)",
                "0901234567",
                "84.000 đ",
                "Upload"
            ),
            Order(
                "#95EA7",
                "21:53 05/12/2025",
                "Nguyễn Văn A (Quận tư viễn)",
                "0901234567",
                "149.000 đ",
                "Upload"
            ),
            Order(
                "#95EC4",
                "15:57 05/12/2025",
                "Nguyễn Văn A (Quận tư viễn)",
                "0901234567",
                "145.000 đ",
                "Upload"
            ),
            Order(
                "#95ECEF",
                "15:13 05/12/2025",
                "Nguyễn Văn A (Quận tư viễn)",
                "0901234567",
                "124.000 đ",
                "Delivering"
            )
        )

        val adapter = OrderAdapter(sampleOrders) { order ->
            // Handle view/edit action
        }

        binding.ordersList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
