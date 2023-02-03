package com.project.babybuy.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.babybuy.PrefManager;
import com.project.babybuy.R;
import com.project.babybuy.login.LoginActivity;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {
    // Initialise Firebase database
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference product_ref = db.collection("notes");
    FirebaseStorage storage;

    EditText etProductName, etProductDescription, etProductPrice;
    ListView list_product;
    Button btn_add, btn_update, btn_delete, btnChooseImage, btnLogout;
    ImageView ivProductImage;
    TextView tvImageUrl;

    // Declare array for product list
    ArrayList<ProductData> productData_item = new ArrayList<>();
    ProductAdapter adapter;
    private String selectedId = ""; // Used to select a product
    private final int PICK_IMAGE_REQUEST = 71;
    URL imageUrl = null;
    String filePath="";
    String  profileImageUrl ="";
    StorageReference storageRef ;
    ProgressBar progressbar_pro ;
    String Purchase = "No";

    // Initialise Firebase for user login
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Check for currently logged in user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        etProductName = findViewById(R.id.etProductName);
        progressbar_pro =  findViewById(R.id.progressbar_pro);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);
        list_product = findViewById(R.id.list_product);
        btn_add = findViewById(R.id.btn_add);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        btnLogout = findViewById(R.id.btnLogout);
        btnChooseImage = findViewById(R.id.btnChooseImage);
//        ivProductImage = findViewById(R.id.ivProductImage);
        tvImageUrl = findViewById(R.id.tvImageUrl);

        storage = FirebaseStorage.getInstance();

        // Connect to Firebase storage
        storageRef= storage.getReferenceFromUrl("gs://babybuy-fae33.appspot.com");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { signOut(); }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        // Add functionality to when a product is selected
        list_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductData productData = adapter.getItem(position);
                etProductName.setText(productData.getTitle());
                etProductDescription.setText(productData.getDescription());
                etProductPrice.setText(productData.getPrice());
                tvImageUrl.setText(productData.getFilePath());
                profileImageUrl = productData.getFilePath();
                selectedId = productData.getDocId();
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        try {
            PrefManager prefManager= new PrefManager(ProductActivity.this);
            prefManager.createLogoutSession();
            btnLogout.setVisibility(View.VISIBLE);
            updateUI();
        } catch (Exception e) {
            showMessage(e.getMessage());
            btnLogout.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void chooseImage() {
        Album.image(this) // Image selection.
                .multipleChoice()
                .camera(true)
                .columnCount(3)
                .selectCount(1)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        filePath = result.get(0).getPath();
                        tvImageUrl.setText(filePath);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        // The user canceled the operation.
                    }
                })
                .start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressbar_pro.setVisibility(View.VISIBLE);
        product_ref.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                progressbar_pro.setVisibility(View.GONE);
                if (e != null) {
                    return;
                }
                productData_item.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    ProductData productData = documentSnapshot.toObject(ProductData.class);
                    productData.setDocId(documentSnapshot.getId());
                    if (productData.getEmail().equals(currentUser.getEmail())) {
                        productData_item.add(productData);
                    }
                }

                // Populate product list items
                adapter = new ProductAdapter(ProductActivity.this, productData_item);
                adapter.notifyDataSetChanged();
                list_product.setAdapter(adapter);

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar_pro.setVisibility(View.VISIBLE);
                if (!tvImageUrl.getText().toString().isEmpty()){

                    // Check for image and prepare for upload
                    Bitmap bmp = null;
                    Uri uri=Uri.fromFile(new File(filePath));
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // Get current date & time as reference for uploaded image
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());

                    final StorageReference childRef = storageRef.child(currentDateandTime);

                    // Uploading the image
                    UploadTask uploadTask = childRef.putFile(uri);


                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            childRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    progressbar_pro.setVisibility(View.GONE);
                                    // Profile image is used as product image
                                    profileImageUrl=task.getResult().toString();
                                    Log.e("URL",profileImageUrl);
                                    addproduct();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressbar_pro.setVisibility(View.GONE);
                            Log.e("Error",e.getLocalizedMessage());
                            Toast.makeText(ProductActivity.this, "Upload Failed -> " + e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                    });
                }else{
                    addproduct();
                }


            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editproduct(v);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteproduct(v);
            }
        });
    }

    public void addproduct() {
        String title = etProductName.getText().toString();
        String description = etProductDescription.getText().toString();
        String price = etProductPrice.getText().toString();
        String imageUrl = tvImageUrl.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put("title", title);
        user.put("description", description);
        user.put("price", price);
        user.put("filePath", profileImageUrl);
        // Logged in user email is used as reference for ownership of product
        user.put("Email", currentUser.getEmail());
        user.put("Purchased", "No");
        if (!title.isEmpty()) {

            product_ref.add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressbar_pro.setVisibility(View.GONE);
                            etProductName.setText(null);
                            etProductDescription.setText(null);
                            etProductPrice.setText(null);
                            tvImageUrl.setText(null);
                            profileImageUrl = "";
                            Toast.makeText(ProductActivity.this, "Success", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressbar_pro.setVisibility(View.GONE);
                            Toast.makeText(ProductActivity.this, "Error" + e, Toast.LENGTH_LONG).show();
                       Log.e("Error",e.getLocalizedMessage());
                        }
                    });
        } else {
            progressbar_pro.setVisibility(View.GONE);
            Toast.makeText(this, "Please select product.", Toast.LENGTH_SHORT).show();
        }
    }

    public void editproduct(View view) {
        String title = etProductName.getText().toString();
        String description = etProductDescription.getText().toString();
        String price = etProductPrice.getText().toString();

        String filePath = profileImageUrl;
        ProductData productData = new ProductData(title, description, price, filePath,currentUser.getEmail(),Purchase);
        if (!title.isEmpty()) {
            product_ref.document(selectedId).set(productData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            etProductName.setText(null);
                            etProductDescription.setText(null);
                            etProductPrice.setText(null);
                            tvImageUrl.setText(null);
                            profileImageUrl="";
                            Toast.makeText(ProductActivity.this, "Updated ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProductActivity.this, "Error " + e, Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please select product.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteproduct(View view) {
        if (!selectedId.equals("")) {
            product_ref.document(selectedId).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            etProductName.setText(null);
                            etProductDescription.setText(null);
                            etProductPrice.setText(null);
                            tvImageUrl.setText(null);
                            profileImageUrl = "";
                            Toast.makeText(ProductActivity.this, "Deleted ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProductActivity.this, "Error " + e, Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please select product.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Prepare image for upload to Firebase storage
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            File myFile = new File(data.getData().getPath());

            try {
                imageUrl = myFile.toURI().toURL();
                Log.e("url", "onActivityResult: " + imageUrl.toString());
                tvImageUrl.setText(imageUrl.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}