package com.example.food_delivery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RecyclerAdapter(): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    private var SpB = FoodItem("Spaghetti Bolognese", "Spaghetti in a beef sauce",12.25,R.drawable.samplefood)
    private var LiP = FoodItem("Linguini Pascatore", "Linguini in a seafood sauce",10.00,R.drawable.samplefood)
    private var AeO = FoodItem("Aglio e olio", "Spaghetti in a garlic and chilli sauce",8.00,R.drawable.samplefood)
    private var StF = FoodItem("Steak Frites", "Steak of choice with fries and bearnaise sauce",20.00,R.drawable.samplefood)
    private var FaC = FoodItem("Fish and chips", "Fried cod and fries",12.50,R.drawable.samplefood)
    private var Sas = FoodItem("Sashimi", "Raw fish innit",5.00,R.drawable.samplefood)
    private var SrR = FoodItem("Spring Rolls", "10x vegetable spring rolls",9.00,R.drawable.samplefood)

    private var Menu = arrayOf<FoodItem>(SpB,LiP,AeO,StF,FaC,Sas,SrR)

    private val FirebaseMenu = Firebase.firestore.collection("foods")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.foodcard_layout, parent, false )

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = Menu[position].getName()
        holder.itemDescription.text = Menu[position].getDescription()
        holder.itemImage.setImageResource(Menu[position].getImage())
        holder.itemPrice.text = Menu[position].getPrice().toString()

    }

    override fun getItemCount(): Int {
        return Menu.size
    }

    class ViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDescription: TextView
        var itemPrice:TextView
        var itemAmount:TextView
        val itemMoneySign : TextView
        val minusButton = itemView.findViewById<ImageButton>(R.id.minusButton)
        val plusButton = itemView.findViewById<ImageButton>(R.id.plusButton)
        val toCartButton = itemView.findViewById<Button>(R.id.cardAddToCartButton)

        init {
            itemImage = itemView.findViewById(R.id.foodImage)
            itemTitle = itemView.findViewById(R.id.foodTitle)
            itemDescription = itemView.findViewById(R.id.foodDescription)
            itemPrice = itemView.findViewById(R.id.foodPrice)
            itemAmount = itemView.findViewById(R.id.foodAmount)
            itemMoneySign = itemView.findViewById(R.id.moneySign)


            var amount = itemAmount.text.toString().toInt()
            amount = 0
            minusButton.visibility = View.INVISIBLE
            toCartButton.visibility = View.INVISIBLE
            itemAmount.text = amount.toString()

            plusButton.setOnClickListener {
                amount++
                itemAmount.text = amount.toString()
                if(amount == 5){
                    plusButton.visibility = View.INVISIBLE
                }
                if(amount > 0) {
                    minusButton.visibility = View.VISIBLE
                }
                if(amount == 0){
                    toCartButton.visibility = View.INVISIBLE
                }
                else{
                    toCartButton.visibility = View.VISIBLE

                }
            }

            minusButton.setOnClickListener {
                amount--
                itemAmount.text = amount.toString()
                if(amount < 1){
                    minusButton.visibility = View.INVISIBLE
                }
                if(amount > 0) {
                    minusButton.visibility = View.VISIBLE
                    plusButton.visibility = View.VISIBLE
                }
                if(amount == 0){
                    toCartButton.visibility = View.INVISIBLE
                }
                else{
                    toCartButton.visibility = View.VISIBLE

                }
            }

            itemAmount.text = amount.toString()

            var totalFee = 0.0
            val db = Firebase.firestore
            val TAG = "TAG"

            toCartButton.setOnClickListener {
                totalFee = amount * itemPrice.text.toString().toDouble()
                var foodTitle = itemTitle.text.toString()

                val order = hashMapOf(
                    "Title" to foodTitle,
                    "Total" to totalFee,
                    "Amount" to amount
                )

                db.collection("Orders")
                    .add(order)
                    .addOnSuccessListener { orderReference ->
                        Log.d(TAG, "Order added: ${orderReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding order", e)
                    }

                amount = 0
                Toast.makeText(itemView.context,"Successfully added to cart", Toast.LENGTH_SHORT).show()
                itemAmount.text = amount.toString()
                minusButton.visibility = View.INVISIBLE
                toCartButton.visibility = View.INVISIBLE

                var amountToAdd = itemAmount.text.toString().toInt()

            }
            itemAmount.text = amount.toString()


        }


    }



}