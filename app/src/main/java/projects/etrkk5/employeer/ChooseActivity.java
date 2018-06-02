package projects.etrkk5.employeer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonEmployee;
    Button buttonCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        buttonEmployee = findViewById(R.id.buttonEmployee);
        buttonCustomer = findViewById(R.id.buttonCustomer);

        buttonEmployee.setOnClickListener(this);
        buttonCustomer.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == buttonEmployee.getId()){
            startActivity(new Intent(ChooseActivity.this, SignUpEmployeeActivity.class));
        }
        if(v.getId() == buttonCustomer.getId()){
            startActivity(new Intent(ChooseActivity.this, SignUpCompanyActivity.class));
        }

    }
}
