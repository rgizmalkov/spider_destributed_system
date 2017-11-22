package com.gmail.rgizmalkov.edu.projects.vo;

import java.util.List;

public interface ServiceManyRs<T>  extends ServiceTechRs{
    List<T> body();
}
