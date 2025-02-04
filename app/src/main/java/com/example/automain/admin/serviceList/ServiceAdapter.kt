package com.example.automain.admin.serviceList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.automain.R

class ServiceAdapter(private val serviceItemList : List<Services>) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_item, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = serviceItemList[position]
        holder.serviceName.text = item.serviceName
        holder.serviceAmount.text = "- ₹ ${item.serviceAmount} rupees only"
        holder.timeRequired.text = "- takes ${item.timeRequired} Hours"
        holder.warranty.text = "- ${item.warranty} Months Warranty"
        holder.recommendationTime.text = "- Every ${item.recommendationTime} Months Recommended"
        holder.numberOfServices.text = "- Include ${item.numberOfServices} Services"
    }

    override fun getItemCount(): Int {
        return serviceItemList.size
    }

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        val serviceAmount: TextView = itemView.findViewById(R.id.serviceAmount)
        val timeRequired: TextView = itemView.findViewById(R.id.timeRequired)
        val recommendationTime: TextView = itemView.findViewById(R.id.recommendationTime)
        val warranty: TextView = itemView.findViewById(R.id.warranty)
        val numberOfServices: TextView = itemView.findViewById(R.id.numberOfServices)
    }
}
