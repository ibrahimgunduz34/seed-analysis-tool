package com.seed.fund.model;

import com.seed.core.MetaData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FundMetaData implements MetaData {
    private final String code;
    private final String name;
}
