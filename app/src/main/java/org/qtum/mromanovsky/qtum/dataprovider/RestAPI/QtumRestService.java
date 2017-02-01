package org.qtum.mromanovsky.qtum.dataprovider.RestAPI;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.OutputUnspent;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface QtumRestService {

    @GET("/outputs/unspent/{address}")
    Call<List<OutputUnspent>> getOutputsUnspent(@Path("address") String address);

}
