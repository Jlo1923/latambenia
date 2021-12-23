package com.example.semillaviva.util

import com.example.semillaviva.ui.adapters.ProductosAdapter

interface OnItemClickListeber {
    fun onItemClick(rv: ProductosAdapter.ViewHolder, position: Int)
}