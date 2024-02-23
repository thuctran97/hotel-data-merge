package com.ascenda.hoteldatamerge.service;

import java.util.List;

public interface DatalakeService {

    void clearCollection();

    List<String> getAllDocuments();

}
