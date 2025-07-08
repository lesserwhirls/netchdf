package com.sunya.netchdf.netcdf4

// TODO run this and see if it fails
class VlenFillValueTest {

    // https://docs.unidata.ucar.edu/netcdf-c/4.9.3/md__2home_2vagrant_2Desktop_2netcdf-c_2docs_2internal.html#intern_vlens
    //
    // There is still one known failure that has not been solved; it is possibly an HDF5 memory leak.
    // All the failures revolve around some variant of this .cdl file. The proximate cause of failure is the use of a VLEN FillValue.
    // netcdf x {
    // types:
    //   float(*) row_of_floats ;
    // dimensions:
    //  m = 5 ;
    // variables:
    //   row_of_floats ragged_array(m) ;
    //       row_of_floats ragged_array:_FillValue = {-999} ;
    // data:
    //   ragged_array = {10, 11, 12, 13, 14}, {20, 21, 22, 23}, {30, 31, 32},
    //                  {40, 41}, _ ;
    // }
}