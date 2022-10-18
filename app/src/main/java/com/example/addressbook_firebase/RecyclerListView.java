package com.example.addressbook_firebase;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecyclerListView extends AppCompatActivity implements RecyclerViewInterface{

    RecyclerView recyclerView;
    ArrayList<AddressModel> arrayList;
    TextView noItemText;

    Button addNew,logoutBtn;
    Handler handler;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_list_view);
        handler=new Handler();
        db= FirebaseFirestore.getInstance();

        noItemText=findViewById(R.id.noItemText);
        logoutBtn=findViewById(R.id.logoutBtn);
        addNew=findViewById(R.id.addNew);
        arrayList=new ArrayList<>();

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        loadData();



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),SigninActivity.class));
                finish();
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RecyclerListView.this,DataEntryForm.class);
                startActivity(i);
            }
        });

    }

    public void onRestart(){
        super.onRestart();

        loadData();

    }

    public void loadData(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                db.collection("users").whereEqualTo("email",FirebaseAuth.getInstance().getCurrentUser().getEmail()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    arrayList.clear();


                                            for(QueryDocumentSnapshot d:task.getResult()){

                                                arrayList.add(new AddressModel(d.getId(),d.getString("name"),d.getString("phone"),d.getString("job"),d.getString("presentAddress"),d.getString("permanentAddress")));


                                            }
                                        }

                                            getData();




                            }
                        });
            }
        });
    }

    public void getData(){
//        arrayList=dbHelper.fetchAddress();

        RecycleAdapter adapter=new RecycleAdapter(this,arrayList, (RecyclerViewInterface) this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(RecyclerListView.this,DetailActivity.class);
//        intent.putExtra("id",arrayList.get(position).getId());
//        intent.putExtra("name",arrayList.get(position).getName());
//        intent.putExtra("phNumber",arrayList.get(position).getPhone());
//        intent.putExtra("present",arrayList.get(position).getPresent_address());
//        intent.putExtra("job",arrayList.get(position).getJob());
//        intent.putExtra("permanent",arrayList.get(position).getPermanent_address());

        startActivity(intent);
    }
}