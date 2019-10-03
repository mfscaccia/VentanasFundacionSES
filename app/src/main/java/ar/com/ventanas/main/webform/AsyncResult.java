package ar.com.ventanas.main.webform;

import org.json.JSONObject;

interface AsyncResult
{
    void onResult(JSONObject object);
}