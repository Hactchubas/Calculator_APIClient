package com.example.calculator_apiclient.activities

import android.R.attr.data
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.calculator_apiclient.R
import com.example.calculator_apiclient.adapter.OperationsAdapter
import com.example.calculator_apiclient.classes.data.OperationsResponse
import com.example.calculator_apiclient.classes.data.RESTOperation
import com.example.calculator_apiclient.listener.OperationRecyclerViewListener
import kotlinx.serialization.json.Json


class MainActivity : AppCompatActivity() {
    private lateinit var operationsAdapter: OperationsAdapter
    private lateinit var operationsListView: RecyclerView
    var queue: RequestQueue? = null

    private var operations = mutableListOf<RESTOperation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        queue = Volley.newRequestQueue(this)
        operationsAdapterInit()
    }

    fun fetchOperations(view: View) {
        val urlStr = "https://calculadora-fxpc.onrender.com/operations"

        val stringRequest = StringRequest(
            Request.Method.GET,
            urlStr,
            { response ->
                Log.v("Resultado", response.toString())
                val responseOperations: OperationsResponse =
                    Json.decodeFromString(OperationsResponse.serializer(), response.toString())
                Log.v("Resultado", responseOperations.toString())
                operationsUpdate(responseOperations)
            },
            {
                Log.v("Resultado", it.message.toString())
            }
        )
        queue?.add(stringRequest)

        setupElementsEvents()
    }

    private fun setupElementsEvents() {
        Log.v("Result", "Entrou")
        // Evento de click
        operationsListView.addOnItemTouchListener(
            OperationRecyclerViewListener(
                applicationContext,
                operationsListView,
                object : OperationRecyclerViewListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val operation = operations[position]
                        var parts = operation.path.split("/")
                        val param1 = findViewById<TextView>(R.id.param1).text
                        val param2 = findViewById<TextView>(R.id.param2).text
                        val urlStr = "https://calculadora-fxpc.onrender.com/${parts[1]}/${parts[2]}/$param1/${param2}"

                        val stringRequest = StringRequest(
                            Request.Method.POST,
                            urlStr,
                            { response ->
                                Log.v("Resultado", response.toString())
                            },
                            {
                                Log.v("Resultado", it.message.toString())
                            }
                        )
                        queue?.add(stringRequest)

                    }
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
                    override fun onLongItemClick(view: View, position: Int) {}

                }
            )
        )
    }

    fun operationsAdapterInit() {
        operationsListView = findViewById(R.id.operationsRecyclerView)
        // Config Adapter
        operationsAdapter = OperationsAdapter(operations, this)
        // Config RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        operationsListView.setHasFixedSize(false)
        operationsListView.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        operationsListView.layoutManager = layoutManager
        operationsListView.adapter = operationsAdapter
    }

    fun operationsUpdate(operationsResponse: OperationsResponse) {
        val size: Int = operations.size
        if (size > 0) {
            for (i in 0 until size) {
                operations.removeAt(0)
            }
            operationsAdapter.notifyItemRangeRemoved(0, size)
        }
        operations.addAll(operationsResponse.operations)
        operationsAdapter.notifyItemRangeInserted(0,operationsResponse.operations.size)
    }
}