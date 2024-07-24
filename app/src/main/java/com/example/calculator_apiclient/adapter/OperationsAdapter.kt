package com.example.calculator_apiclient.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator_apiclient.R
import com.example.calculator_apiclient.classes.data.RESTOperation

class OperationsAdapter (var operationsList: MutableList<RESTOperation>, var context: Activity) :
    RecyclerView.Adapter<OperationsAdapter.Operation>() {
    inner class Operation(itemView : View): RecyclerView.ViewHolder(itemView) {
        var operation = itemView.findViewById<Button>(R.id.operationButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Operation {
        val operation : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.operation_layout, parent, false)

        return Operation(operation)
    }

    override fun getItemCount(): Int {
        return operationsList.size
    }

    override fun onBindViewHolder(holder: Operation, position: Int) {
        val currentItem = operationsList[position]
        holder.operation.text = currentItem.name
    }

}