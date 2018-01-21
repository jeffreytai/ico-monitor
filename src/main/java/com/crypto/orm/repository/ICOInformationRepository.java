package com.crypto.orm.repository;

import com.crypto.orm.entity.ICOInformation;
import com.crypto.utils.DbUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ICOInformationRepository {

    /**
     * Find a ICO entry by the codename
     * @param codeName
     * @return
     */
    public static ICOInformation findByCodeName(String codeName) {
        String query = "SELECT i FROM ICOInformation i where i.codeName = :codeName";
        Map<Object, Object> bindedParameters = new HashMap<>();
        bindedParameters.put("codeName", codeName);

        ICOInformation result = (ICOInformation) DbUtils.runSingularResultQuery(query, bindedParameters);
        return result;
    }

    /**
     * Find a ICO entry by the name
     * @param name
     * @return
     */
    public static ICOInformation findByName(String name) {
        String query = "SELECT i from ICOInformation i where i.name = :name";
        Map<Object, Object> bindedParameters = new HashMap<>();
        bindedParameters.put("name", name);

        ICOInformation result = (ICOInformation) DbUtils.runSingularResultQuery(query, bindedParameters);
        return result;
    }

    public static List<ICOInformation> findAll() {
        String query = "SELECT i from ICOInformation i";
        List<ICOInformation> result = (List<ICOInformation>) DbUtils.runMultipleResultQuery(query);
        return result;
    }
}
