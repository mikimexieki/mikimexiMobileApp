package com.calderon.mikimexiapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.calderon.mikimexiapp.activities.ClientesActivity;
import com.calderon.mikimexiapp.activities.LoginCliente;
import com.calderon.mikimexiapp.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static android.content.Context.VIBRATOR_SERVICE;

public class Util {


    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static final int SMS = 301;
    public static final int CALL = 302;
    public static final int PEDIDO = 303;

    public static final int CLIENTES = 2301;
    public static final int VENDEDORES = 2302;
    public static final int REPARTIDORES = 2303;

    public static final String ID = "id";
    public static final String TYPE_USER = "typeUser";
    public static final String NOMBRE = "nombre";

    public static String nombre;

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static DocumentReference doc;

    public static void saveTypeUser(SharedPreferences prefs, int type, String id){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(TYPE_USER,type);
        editor.putString(ID, id);
        editor.apply();
    }

    public static int getTypeUser(SharedPreferences prefs){
        return prefs.getInt(TYPE_USER,0);
    }

    public static String getIdUser(SharedPreferences prefs){
        return prefs.getString(ID,"");
    }

    public static void removeUser(SharedPreferences prefs){
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(ID);
        editor.remove(TYPE_USER);
        editor.remove(NOMBRE);
        editor.apply();
    }

    public static void signIn(final Activity activity,
                              final Class<?> classT,
                              final String id,
                              final String email,
                              String pass,
                              final int type,
                              final SharedPreferences prefs){
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveNombre(activity,type,id,prefs);
                            saveTypeUser(prefs, type, id);
                            Intent intent = new Intent(activity, classT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);
                        } else {
                            Toast.makeText(activity, "Verificue sus datos",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private static void saveNombre(Activity activity, int tipo, String id, final SharedPreferences prefs){
        final String[] s = new String[1];
        Log.i("+++++++++++++++++++",id);
        if (tipo == VENDEDORES) {
            doc = db.document("vendedores/" + id);
            doc.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    s[0] = documentSnapshot.getString("nombre");
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(NOMBRE,s[0]);
                    editor.apply();
                }
            });
        }

        if (tipo == CLIENTES) {
            doc = db.document("clientes/" + id);
            doc.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    s[0] = documentSnapshot.getString("nombre");
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(NOMBRE,s[0]);
                    editor.apply();
                }
            });
        }

        if (tipo == REPARTIDORES) {
            doc = db.document("repartidores/" + id);
            doc.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    s[0] = documentSnapshot.getString("nombre");
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(NOMBRE,s[0]);
                    editor.apply();
                }
            });
        }
    }

    public static boolean validateEditText(TextInputLayout textInputLayout, Context context) {
        String text = textInputLayout.getEditText().getText().toString().trim();
        if (text.isEmpty()) {
            textInputLayout.setError("Campo requerido");
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, 10));
            } else {
                ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(150);
            }
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    public static void signOut(final Activity activity, final SharedPreferences prefs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("¿Está seguro de querer salir de su cuenta?");
        builder.setCancelable(false);
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                removeUser(prefs);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public static String getNombre(SharedPreferences sp){
        return  sp.getString(NOMBRE,"");
    }

}
