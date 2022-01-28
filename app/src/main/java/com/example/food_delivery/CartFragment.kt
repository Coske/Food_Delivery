package com.example.food_delivery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder


class CartFragment : Fragment() {

    lateinit var personName : EditText
    lateinit var personAddress : EditText
    lateinit var personContact : EditText

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<OrderAdapter.OrderViewHolder>? = null


    //private var layoutManager: RecyclerView.LayoutManager? = null
    //private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    private lateinit var recyclerView : RecyclerView
    private lateinit var orderList : ArrayList<OrderItem>
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val completeOrderButton = view.findViewById<Button>(R.id.completeButton)
        val totalText = view.findViewById<TextView>(R.id.totalText)
        val euroSign = view.findViewById<TextView>(R.id.euro)
        val checkButton = view.findViewById<Button>(R.id.checkOrderButton)
        val orderForName = view.findViewById<EditText>(R.id.yourName)
        val orderForAddress = view.findViewById<EditText>(R.id.yourAddress)
        val orderForContact = view.findViewById<EditText>(R.id.yourContact)
//        var order = arrayListOf<String>()
//        var part_order = arguments?.getString("sampleArg")
//        order.add(part_order.toString())
//
//        var sb = StringBuilder()
//
//        sb.append(order)
        totalText.text = ""
        euroSign.text = ""

        db = FirebaseFirestore.getInstance()
        orderList = arrayListOf()
        orderAdapter = OrderAdapter(orderList)
        var sb = StringBuilder()
        var totalPrice = 0.0
        val TAG = "TAG";

        checkButton.setOnClickListener {

            db.collection("Orders").
                    addSnapshotListener(object : EventListener<QuerySnapshot>{
                        override fun onEvent(
                            value: QuerySnapshot?,
                            error: FirebaseFirestoreException?
                        ) {
                            if(error != null){
                                Log.e("FIRESTORE ERROR", error.message.toString())
                            }

                            for(dc : DocumentChange in value?.documentChanges!!){
                                if(dc.type == DocumentChange.Type.ADDED){
                                    var orderItem = dc.document.toObject(OrderItem::class.java)
                                    orderList.add(orderItem)
                                    totalPrice += orderItem.Total.toString().toDouble()
                                }
                            }
                            totalText.text = totalPrice.toString()
                            euroSign.text = "€"
                            orderAdapter.notifyDataSetChanged()
                            sb.append(orderList)
                            //textview.text = sb
                        }
                    })

            checkButton.visibility = View.INVISIBLE

//            textview.text = sb.toString()
//            val users = db.collection("users").get()
//                .addOnSuccessListener { querySnapshot ->
//                    querySnapshot.forEach { document ->
//                        Log.d(TAG, "Read document with ID ${document.data}")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.w(TAG, "Error getting documents $exception")
//                }
//            Toast.makeText(context,users.toString(),Toast.LENGTH_SHORT).show()
        }

        completeOrderButton.setOnClickListener {
            val name = orderForName.text.toString()
            orderList.removeAll(orderList)
            orderAdapter.notifyDataSetChanged()

            db.collection("Orders")
                .get()
                .addOnSuccessListener { querySnapshot->
                    querySnapshot.forEach{
                        document -> document.reference.delete()
                    }
                }

            checkButton.visibility = View.VISIBLE
            Toast.makeText(this.context,"${name}, Your order is complete", Toast.LENGTH_SHORT).show()
            totalPrice = 0.0
            euroSign.text = ""
            totalText.text = ""
            orderForName.text.clear()
            orderForAddress.text.clear()
            orderForContact.text.clear()
        }


        return view
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        super.onViewCreated(itemView, savedInstanceState)
        var recyclerView = itemView.findViewById<RecyclerView>(R.id.cartRecyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = orderAdapter
        }
    }

}