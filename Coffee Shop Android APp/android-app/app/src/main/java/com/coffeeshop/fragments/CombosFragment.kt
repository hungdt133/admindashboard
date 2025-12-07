package com.coffeeshop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.coffeeshop.databinding.FragmentCombosBinding
import com.coffeeshop.models.Combo
import com.coffeeshop.adapters.ComboAdapter

class CombosFragment : Fragment() {

    private var _binding: FragmentCombosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCombosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sample data
        val sampleCombos = listOf(
            Combo(
                "1",
                "Combo Sáng Tinh Tảo",
                "Nút đó ngon với đây uống vả cà phê đặc thêm combo breakfast thơm lừng",
                listOf("1 cà phê đen", "1 bánh croissant"),
                "89.000 đ"
            ),
            Combo(
                "2",
                "Combo Trà Bạnh Chill",
                "Sự hòa quyện báo giác ở vệ tinh nước cà lạnh ngon ơi",
                listOf("1 trà đen lạnh", "1 bánh trung thu"),
                "79.000 đ"
            ),
            Combo(
                "3",
                "Combo Báo Ngày",
                "Ví báo cái bể mua quang cùng những niều ưa thích",
                listOf("2 báo", "1 bánh mì"),
                "65.000 đ"
            ),
            Combo(
                "4",
                "Combo Bữa Xế",
                "Nạp năng lượng buổi chiều, uống với bài tốc độ ơi bánh bộ",
                listOf("1 trà ít bưởi", "1 bánh croissant"),
                "75.000 đ"
            ),
            Combo(
                "5",
                "Combo Dải Bạn Thân",
                "Mua 2 lại teo với những ưu tiên, chỉ từ mộ giá ưa thích",
                listOf("2 vỉ", "1 bánh trung thu"),
                "49.000 đ"
            ),
            Combo(
                "6",
                "Combo Sáng Tinh Tảo",
                "Nút đó ngon với đây uống vả cà phê đặc thêm combo breakfast thơm lừng",
                listOf("1 cà phê đen", "1 bánh croissant"),
                "89.000 đ"
            )
        )

        val adapter = ComboAdapter(
            sampleCombos,
            { combo ->
                // Handle edit
            },
            { combo ->
                // Handle delete
            }
        )

        binding.combosGrid.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
        }

        binding.btnAddCombo.setOnClickListener {
            // Handle add new combo
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
