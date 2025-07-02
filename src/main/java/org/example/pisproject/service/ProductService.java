package org.example.pisproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    // We generally don’t want to shard products, since they’re global/catalog-level data.

}
