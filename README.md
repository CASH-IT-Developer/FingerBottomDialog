# Finger Bottom Dialog
Library for finger bottomDialog

Waiting a finger           |  Success Login
:-------------------------:|:-------------------------:
![alt text](https://raw.githubusercontent.com/CASH-IT-Developer/FingerBottomDialog/master/finger1.jpeg)  |  ![alt text](https://raw.githubusercontent.com/CASH-IT-Developer/FingerBottomDialog/master/finger2.jpeg)



### How To Use
     implementation "com.github.CASH-IT-Developer:FingerBottomDialog:1.0.0"

### Implementation in Programatically

    public class MainActivity extends AppCompatActivity implements cashItGlobalInterface.CashitFingerDlgCallback {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
    
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cashItFingerBottomDlg.isAvailable(MainActivity.this)) {
                        cashItFingerBottomDlg.initialize(MainActivity.this)
                                .title("Masuk menggunakan fingerprint")
                                .message("Letakkan jari anda difingerprint")
                                .callback(MainActivity.this)
                                .show();
                    }
                }
            });
        }
        
    
        @Override
        public void onAuthenticationSucceeded() {
            Log.d("MainActivity", "onAuthenticationSucceeded: ");
        }
    
        @Override
        public void onAuthenticationCancel() {
            Log.d("MainActivity", "onAuthenticationCancel: ");
        }
    }
        
        
   ### Code by vickykdv (https://github.com/vickyKDV) 
   
   
   ### License
          
    Copyright 2020 PT. Kreigan Sentral Teknologi
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
          
    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.