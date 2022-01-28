package com.example.food_delivery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class OrderAdapter (private val orderList : ArrayList<OrderItem>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderAdapter.OrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false )

        return OrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrderAdapter.OrderViewHolder, position: Int) {
        val order : OrderItem = orderList[position]
        holder.title.text = order.Title
        holder.price.text = order.Total.toString()
        holder.amount.text = order.Amount.toString()
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    public class OrderViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){

        var amount : TextView
        var price : TextView
        var title : TextView

        init{
            amount = itemView.findViewById(R.id.itemAmount)
            price = itemView.findViewById(R.id.itemPrice)
            title = itemView.findViewById(R.id.itemTitle)
        }

    }
}