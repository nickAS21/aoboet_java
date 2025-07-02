package io.dcloud.feature.ui.nativeui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class NativeUIFeatureImpl {

    private final Activity activity;
    private final WebView webView;

    public NativeUIFeatureImpl(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }

    // Imitation of 'execute' method
    public void execute(String action, String[] args) {
        try {
            switch (action) {
                case "alert": {
                    JSONArray arr = new JSONArray(args[0]);
                    String message = arr.optString(0);
                    String title = arr.optString(1);
                    String button = arr.optString(2, "OK");
                    String callback = arr.optString(3);
                    showAlert(title, message, button, callback);
                    break;
                }

                case "confirm": {
                    JSONArray arr = new JSONArray(args[0]);
                    String message = arr.optString(0);
                    String title = arr.optString(1);
                    JSONArray buttons = arr.optJSONArray(2);
                    String callback = arr.optString(3);
                    showConfirm(title, message, buttons, callback);
                    break;
                }

                case "prompt": {
                    JSONArray arr = new JSONArray(args[0]);
                    String message = arr.optString(0);
                    String title = arr.optString(1);
                    String defaultText = arr.optString(2);
                    JSONArray buttons = arr.optJSONArray(3);
                    String callback = arr.optString(4);
                    showPrompt(title, message, defaultText, buttons, callback);
                    break;
                }

                case "toast": {
                    JSONObject obj = new JSONObject(args[0]);
                    String msg = obj.optString("message", "");
                    boolean longTime = obj.optBoolean("long", false);
                    showToast(msg, longTime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                    break;
                }

                case "pickDate": {
                    String callback = args[0];
                    showDatePicker(callback);
                    break;
                }

                case "pickTime": {
                    String callback = args[0];
                    showTimePicker(callback);
                    break;
                }

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, String button, String callback) {
        activity.runOnUiThread(() -> {
            new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(button, (dialog, which) -> {
                        if (!TextUtils.isEmpty(callback)) {
                            evaluate(callback, "{index:0}");
                        }
                    })
                    .setCancelable(false)
                    .show();
        });
    }

    private void showConfirm(String title, String message, JSONArray buttons, String callback) {
        activity.runOnUiThread(() -> {
            String positive = buttons.optString(0, "OK");
            String negative = buttons.optString(1, "Cancel");
            new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positive, (d, w) -> evaluate(callback, "{index:0}"))
                    .setNegativeButton(negative, (d, w) -> evaluate(callback, "{index:1}"))
                    .setCancelable(false)
                    .show();
        });
    }

    private void showPrompt(String title, String message, String defaultText, JSONArray buttons, String callback) {
        activity.runOnUiThread(() -> {
            EditText input = new EditText(activity);
            input.setText(defaultText);

            String positive = buttons.optString(0, "OK");
            String negative = buttons.optString(1, "Cancel");

            new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setView(input)
                    .setPositiveButton(positive, (dialog, which) -> {
                        String val = input.getText().toString();
                        evaluate(callback, "{index:0,message:'" + escape(val) + "'}");
                    })
                    .setNegativeButton(negative, (dialog, which) -> {
                        String val = input.getText().toString();
                        evaluate(callback, "{index:1,message:'" + escape(val) + "'}");
                    })
                    .setCancelable(false)
                    .show();
        });
    }

    private void showToast(String message, int duration) {
        activity.runOnUiThread(() -> Toast.makeText(activity, message, duration).show());
    }

    public void showDatePicker(String callback) {
        Calendar calendar = Calendar.getInstance();
        activity.runOnUiThread(() -> {
            new DatePickerDialog(activity, (view, y, m, d) -> {
                Calendar selected = Calendar.getInstance();
                selected.set(y, m, d, 0, 0, 0);
                evaluate(callback, String.valueOf(selected.getTimeInMillis()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void showTimePicker(String callback) {
        Calendar calendar = Calendar.getInstance();
        activity.runOnUiThread(() -> {
            new TimePickerDialog(activity, (view, h, m) -> {
                Calendar selected = Calendar.getInstance();
                selected.set(Calendar.HOUR_OF_DAY, h);
                selected.set(Calendar.MINUTE, m);
                evaluate(callback, String.valueOf(selected.getTimeInMillis()));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });
    }

    private void evaluate(String callback, String payload) {
        if (!TextUtils.isEmpty(callback)) {
            webView.post(() -> webView.evaluateJavascript(callback + "(" + payload + ")", null));
        }
    }

    private String escape(String s) {
        return s.replace("'", "\\'");
    }
}