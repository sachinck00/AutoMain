package com.example.automain.admin.serviceList

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.automain.R
import com.example.automain.admin.AdminActivity
import com.example.automain.user.UserActivity
import com.example.components.fetchCurrentUserEmail
import com.google.firebase.firestore.FirebaseFirestore


class ServiceAdapter(private val serviceItemList : List<Services>) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {
    private lateinit var db : FirebaseFirestore
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

        holder.itemView.setOnClickListener {
            db = FirebaseFirestore.getInstance()
            val email = fetchCurrentUserEmail()
            val context = it.context

            // Create an Intent to start the ServiceDetailsActivity
            db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.get("isAdmin") == true) {
                            //edit service
                            val intent = Intent(context, EditServiceActivity::class.java).apply {
                                // Bundle for passing multiple values
                                putExtra("SERVICE_NAME", item.serviceName)
                                putExtra("SERVICE_AMOUNT", "- ₹ ${item.serviceAmount} rupees only")
                                putExtra("TIME_REQUIRED", "- takes ${item.timeRequired} Hours")
                                putExtra("WARRANTY", "- ${item.warranty} Months Warranty")
                                putExtra("RECOMMENDATION_TIME", "- Every ${item.recommendationTime} Months Recommended")
                                putExtra("NUMBER_OF_SERVICES", "- Include ${item.numberOfServices} Services")
                            }

                            context.startActivity(intent)
                        }else {

                            val intent = Intent(context, ServiceDetailsActivity::class.java).apply {
                                // Bundle for passing multiple values
                                putExtra("SERVICE_NAME", item.serviceName)
                                putExtra("SERVICE_AMOUNT", "- ₹ ${item.serviceAmount} rupees only")
                                putExtra("TIME_REQUIRED", "- takes ${item.timeRequired} Hours")
                                putExtra("WARRANTY", "- ${item.warranty} Months Warranty")
                                putExtra("RECOMMENDATION_TIME", "- Every ${item.recommendationTime} Months Recommended")
                                putExtra("NUMBER_OF_SERVICES", "- Include ${item.numberOfServices} Services")
                            }
                            context.startActivity(intent)
                        }


                    }
                }
        }
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
