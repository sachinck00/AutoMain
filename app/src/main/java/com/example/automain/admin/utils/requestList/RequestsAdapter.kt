package com.example.automain.admin.utils.requestList

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.automain.R
import com.example.components.getNameWithEmail

class RequestsAdapter(private val requestsList : List<Requests>) : RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestsAdapter.RequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.request_item, parent, false)
        return RequestViewHolder(view)

    }

    override fun onBindViewHolder(holder: RequestsAdapter.RequestViewHolder, position: Int) {
        val item = requestsList[position]
        holder.serviceName.text = item.serviceName
         getNameWithEmail(item.userEmail) { name ->
            if(name != ""){
             holder.userEmail.text = "Requested by ${name?.capitalize()}"
            }
        }
        holder.time.text = "At ${item.time}"
        /*holder.location.text = item.location
        holder.vehicleName.text = item.vehicleName
        holder.vehicleModel.text = item.vehicleModel
        holder.vehicleNumber.text = item.vehicleNumber*/
        holder.viewBtn.setOnClickListener{
            val context = it.context
            var intent = Intent(context , RequestDetailsActivity::class.java).apply {
                putExtra("serviceName", item.serviceName)
                putExtra("userEmail", item.userEmail)
                putExtra("time", item.time)
                putExtra("location", item.location)
                putExtra("vehicleModel", item.vehicleModel)
                putExtra("vehicleNumber", item.vehicleNumber)
                putExtra("vehicleName", item.vehicleName)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return requestsList.size
    }
    inner class RequestViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val serviceName : TextView = itemView.findViewById(R.id.serviceName)
        val userEmail : TextView = itemView.findViewById(R.id.userEmail)
        val time : TextView = itemView.findViewById(R.id.time)
       /* val location : TextView = itemView.findViewById(R.id.location)
        val vehicleName : TextView = itemView.findViewById(R.id.vehicleName)
        val vehicleModel : TextView = itemView.findViewById(R.id.vehicleModel)
        val vehicleNumber : TextView = itemView.findViewById(R.id.vehicleNumber)*/
        val viewBtn : TextView = itemView.findViewById(R.id.viewBtn)
    }
}