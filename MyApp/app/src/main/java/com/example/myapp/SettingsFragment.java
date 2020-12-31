package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class SettingsFragment extends Fragment {

    private ListView list;
    private String[] items = {
            "Nota: esta actividad aún no ha sido implementada",
            "Cambiar correo electrónico",
            "Cambiar nombre de usuario",
            "Cambiar contraseña",
            "Vincular otros servicios",
            "Pantalla",
            "Notificaciones por correo electrónico",
            "Controles de privacidad",
            "Permisos de datos", "Soporte técnico",
            "Preguntas frecuentes",
            "Legal",
            "Acerca de",
            "Cerrar sesión"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        list = view.findViewById(R.id.settingsListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                if(position==13) {
                    User.reset();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.fadein,R.transition.fadeout);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Ha pulsado el item " + position, Toast.LENGTH_SHORT).show();
                }
            }

        });
        return view;
    }
}
