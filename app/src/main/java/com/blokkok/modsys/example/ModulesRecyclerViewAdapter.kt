package com.blokkok.modsys.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.blokkok.modsys.ModuleManager
import com.blokkok.modsys.models.ModuleMetadata
import com.google.android.material.switchmaterial.SwitchMaterial

class ModulesRecyclerViewAdapter : RecyclerView.Adapter<ModulesRecyclerViewAdapter.ViewHolder>() {

    var modules: ArrayList<ModuleMetadata> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.module_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentModule = modules[position]

        holder.name.text = currentModule.name
        holder.description.text = currentModule.description
        holder.enableSwitch.isChecked = currentModule.enabled

        holder.enableSwitch.setOnCheckedChangeListener { _, checked ->
            if (checked) ModuleManager.enableModule (currentModule.id)
            else         ModuleManager.disableModule(currentModule.id)
        }

        holder.root.setOnLongClickListener {

            AlertDialog.Builder(it.context)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to delete the module \"${currentModule.name}\"?")
                .setPositiveButton("Yes") { d, _ ->
                    ModuleManager.deleteModule(currentModule.id)
                    Toast.makeText(it.context,
                        "Module ${currentModule.name} has been deleted", Toast.LENGTH_SHORT).show()

                    modules.remove(currentModule)
                    notifyItemRemoved(holder.adapterPosition)

                    d.dismiss()
                }
                .setNegativeButton("No") { d, _ ->
                    d.dismiss()
                }
                .create()
                .show()

            true
        }
    }

    override fun getItemCount(): Int = modules.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: View = itemView.findViewById(R.id.module_root)
        val name: TextView = itemView.findViewById(R.id.module_name)
        val description: TextView = itemView.findViewById(R.id.module_description)
        val enableSwitch: SwitchMaterial = itemView.findViewById(R.id.enable_switch)
    }
}