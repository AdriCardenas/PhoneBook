package com.example.adrian.agenda;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 04/11/2017.
 */

public class ContactoRecyclerViewAdapter extends RecyclerView.Adapter<ContactoRecyclerViewAdapter.ContactoViewHolder> implements Filterable {
    private List<Contacto> contactos = new ArrayList<>();
    private List<Contacto> contactosFiltrados = new ArrayList<>();
    private Context context;
    private Agenda agenda;

    public ContactoRecyclerViewAdapter(List<Contacto> listContactos, Context context) {
        contactos = listContactos;
        contactosFiltrados = listContactos;
        this.context = context;

        agenda = (Agenda) SingletonMap.getInstance().get(MainActivity.nombre);
        if(agenda == null){
            agenda = new Agenda(context);
            SingletonMap.getInstance().put(MainActivity.nombre, agenda);
        }
    }

    @Override
    public ContactoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_contacto, parent, false);
        ContactoViewHolder contactoViewHolder = new ContactoViewHolder(v, context, contactosFiltrados);
        return contactoViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactoViewHolder holder, int position) {
        Contacto contacto = contactosFiltrados.get(position);
        holder.contactName.setText(contacto.getNombre());
        holder.contactNumber.setText(contacto.getNumero());
    }

    @Override
    public int getItemCount() {
        return this.contactosFiltrados.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    Cursor res = agenda.getAllData();
                    contactos = new ArrayList<>();

                    if (res.getCount() == 0)
                        Toast.makeText(context, context.getResources().getString(R.string.no_contacs), Toast.LENGTH_LONG).show();
                    else {
                        while (res.moveToNext()) {
                            Contacto c = new Contacto(res.getString(0), res.getString(1), res.getString(2));
                            contactos.add(c);
                        }
                    }
                    contactosFiltrados = contactos;
                }else{
                    contactosFiltrados = agenda.searchUser(charString);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=contactosFiltrados;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactosFiltrados= (List<Contacto>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class ContactoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View view;

        private CardView cardView;
        private ImageView imageView;
        private TextView contactName;
        private TextView contactNumber;
        private Context context;
        private List<Contacto> listaContactos;

        public ContactoViewHolder(View itemView, final Context context, List<Contacto> listaContactos) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.context = context;
            this.listaContactos = listaContactos;

            cardView = itemView.findViewById(R.id.cardView_contacto);
            imageView = itemView.findViewById(R.id.image_llamada);
            contactName = itemView.findViewById(R.id.contact_name);
            contactNumber = itemView.findViewById(R.id.contact_number);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+contactNumber.getText().toString()));
                        context.startActivity(intent);
                        return;
                    }else{
                        Toast.makeText(context, context.getResources().getString(R.string.request_permission), Toast.LENGTH_LONG);
                    }
                }
            });


        }

        public CardView getCardView() {
            return cardView;
        }

        public TextView getContactName() {
            return contactName;
        }

        public TextView getContactNumber() {
            return contactNumber;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent intent = new Intent(this.context, ContactoActivity.class);
            intent.putExtra("nombre", listaContactos.get(position).getNombre());
            intent.putExtra("numero", listaContactos.get(position).getNumero());
            this.context.startActivity(intent);
        }

    }

}
