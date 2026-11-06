package com.tecsup.rincondevallejo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.rincondevallejo.R
import com.tecsup.rincondevallejo.data.Sede

class SedeAdapter(
    private val onSedeClick: (Sede) -> Unit,
    private val onSedeLongClick: ((Sede) -> Unit)? = null
) : RecyclerView.Adapter<SedeAdapter.SedeViewHolder>() {

    private var sedesList: List<Sede> = emptyList()

    class SedeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreSede: TextView = itemView.findViewById(R.id.tvItemNombreSede)
        val tvEspecialidad: TextView = itemView.findViewById(R.id.tvItemEspecialidad)
        val tvCapacidad: TextView = itemView.findViewById(R.id.tvItemCapacidad)
        val tvMesas: TextView = itemView.findViewById(R.id.tvItemMesas)
        val tvGerente: TextView = itemView.findViewById(R.id.tvItemGerente)
        val tvJefePersonal: TextView = itemView.findViewById(R.id.tvItemJefePersonal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SedeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sede, parent, false)
        return SedeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SedeViewHolder, position: Int) {
        val sede = sedesList[position]
        holder.tvNombreSede.text = "RINCÓN DE VALLEJO - ${sede.nombre}"
        holder.tvEspecialidad.text = "Especialidad: ${sede.especialidad}"
        holder.tvCapacidad.text = "${sede.capacidad} personas"
        holder.tvMesas.text = "${sede.mesas} mesas"
        holder.tvGerente.text = "Gerente: ${sede.gerente}"
        holder.tvJefePersonal.text = "Jefe de Personal: ${sede.jefePersonal}"

        holder.itemView.setOnClickListener {
            onSedeClick(sede)
        }

        holder.itemView.setOnLongClickListener {
            onSedeLongClick?.invoke(sede)
            true
        }
    }

    override fun getItemCount(): Int = sedesList.size

    fun updateSedes(newSedes: List<Sede>) {
        sedesList = newSedes
        notifyDataSetChanged()
    }
}
