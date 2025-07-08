package com.sunya.netchdf


import kotlin.test.*
import com.sunya.netchdf.testfiles.testData

// Special test for NPP data.
class TestNPP {

/* ncdump barfs on reference im guessing
    ./ncdump /home/all/testdata/netchdf/npp/VCBHO_npp_d20030125_t084955_e085121_b00015_c20071213022754_den_OPS_SEG.h5 -h

netcdf VCBHO_npp_d20030125_t084955_e085121_b00015_c20071213022754_den_OPS_SEG {

// global attributes:
		string :Instrument_Short_Name = "VIIRS" ;
		string :Mission_Name = "NPP" ;
		string :N_Dataset_Source = "Ikbaker" ;
		string :N_GEO_Ref = "VIIRS-CLD-AGG-GEO" ;
		string :N_HDF_Creation_Date = "20071213" ;
		string :N_HDF_Creation_Time = "022754.500487Z" ;
		string :N_Software_Version = "I1.5.0.15.1" ;
		string :Platform_Short_Name = "NPP" ;

group: All_Data {

  group: VIIRS-CBH-EDR_All {
    dimensions:
    	phony_dim_0 = UNLIMITED ; // (2 currently)
    	phony_dim_1 = UNLIMITED ; // (96 currently)
    	phony_dim_2 = UNLIMITED ; // (508 currently)
    	phony_dim_3 = UNLIMITED ; // (4 currently)
    variables:
    	float CloudBaseHeightFactors(phony_dim_0) ;
    	ushort CloudBaseHeightLayer(phony_dim_1, phony_dim_2, phony_dim_3) ;
    	ushort CloudBaseHeightTotal(phony_dim_1, phony_dim_2) ;
    	ubyte QF1_VIIRSCBHLAYEREDR(phony_dim_1, phony_dim_2, phony_dim_3) ;
    	ubyte QF2_VIIRSCBHLAYEREDR(phony_dim_1, phony_dim_2, phony_dim_3) ;
    	ubyte QF3_VIIRSCBHTOTALEDR(phony_dim_1, phony_dim_2) ;
    	ubyte QF4_VIIRSCBHTOTALEDR(phony_dim_1, phony_dim_2) ;
    	ubyte QF5_VIIRSCBHTOTALEDR(phony_dim_1, phony_dim_2) ;
    	ubyte QF6_VIIRSCBHTOTALEDR(phony_dim_1, phony_dim_2) ;
    } // group VIIRS-CBH-EDR_All
  } // group All_Data

group: Data_Products {

  group: VIIRS-CBH-EDR {
NetCDF: Can't open HDF5 attribute
Location: file ncdump.c; fcn do_ncdump_rec line 1669

////////////////////////////////////
netchdf and H5File has the same cdl:

    netchdf = netcdf VCBHO_npp_d20030125_t084955_e085121_b00015_c20071213022754_den_OPS_SEG.h5 {

  // global attributes:
  :Instrument_Short_Name = "VIIRS" ;
  :Mission_Name = "NPP" ;
  :N_Dataset_Source = "Ikbaker" ;
  :N_GEO_Ref = "VIIRS-CLD-AGG-GEO" ;
  :N_HDF_Creation_Date = "20071213" ;
  :N_HDF_Creation_Time = "022754.500487Z" ;
  :N_Software_Version = "I1.5.0.15.1" ;
  :Platform_Short_Name = "NPP" ;

  group: All_Data {

    group: VIIRS-CBH-EDR_All {
      variables:
        float CloudBaseHeightFactors(2) ;
        ushort CloudBaseHeightLayer(96, 508, 4) ;
        ushort CloudBaseHeightTotal(96, 508) ;
        ubyte QF1_VIIRSCBHLAYEREDR(96, 508, 4) ;
        ubyte QF2_VIIRSCBHLAYEREDR(96, 508, 4) ;
        ubyte QF3_VIIRSCBHTOTALEDR(96, 508) ;
        ubyte QF4_VIIRSCBHTOTALEDR(96, 508) ;
        ubyte QF5_VIIRSCBHTOTALEDR(96, 508) ;
        ubyte QF6_VIIRSCBHTOTALEDR(96, 508) ;
    }
  }

  group: Data_Products {

    group: VIIRS-CBH-EDR {
      variables:
        reference VIIRS-CBH-EDR_Aggr(9) ;
          :AggregateBeginningDate = "20030125" ;
          :AggregateBeginningGranuleID = "NPP001212077949" ;
          :AggregateBeginningOrbitNumber = 15 ;
          :AggregateBeginningTime = "084955.996288Z" ;
          :AggregateEndingDate = "20030125" ;
          :AggregateEndingGranuleID = "NPP001212077949" ;
          :AggregateEndingOrbitNumber = 15 ;
          :AggregateEndingTime = "085121.449152Z" ;
          :AggregateNumberGranules = 1 ;
        reference VIIRS-CBH-EDR_Gran_0(9) ;
          :Ascending_Descending_Indicator = "0" ;
          :Beginning_Date = "20030125" ;
          :Beginning_Time = "084955.996288Z" ;
          :Cloud_Cover = 37.0401f ;
          :East_Bounding_Coordinate = 81.7209f ;
          :Ending_Date = "20030125" ;
          :Ending_Time = "085121.449152Z" ;
          :G-Ring_Latitude = 26.973f, 26.6646f, 26.3552f, 26.0465f, 25.7372f, 25.4284f, 25.1195f, 24.8105f, 24.5016f, 24.1926f, 23.8835f, 23.4529f, 23.2952f, 23.1553f, 23.0309f, 22.8363f, 22.6512f, 22.4125f, 22.1913f, 21.9722f, 21.7373f, 21.4633f, 21.1152f, 20.7962f, 20.3986f, 20.0972f, 19.6944f, 19.1064f, 19.4062f, 19.7055f, 20.0049f, 20.3034f, 20.6025f, 20.9011f, 21.1999f, 21.498f, 21.7967f, 22.0942f, 22.3921f, 22.6897f, 22.9874f, 23.2847f, 23.582f, 23.9967f, 24.6219f, 25.0456f, 25.3609f, 25.7752f, 26.1056f, 26.4655f, 26.7491f, 26.9931f, 27.222f, 27.4543f, 27.7053f, 27.8996f, 28.1016f, 28.2289f, 28.3687f, 28.5182f, 28.209f, 27.9001f, 27.5908f, 27.2823f ;
          :G-Ring_Longitude = 81.7209f, 79.1473f, 77.2996f, 75.8604f, 73.8697f, 72.1864f, 70.2348f, 68.5943f, 67.0946f, 65.6f, 63.9761f, 62.061f, 60.4206f, 58.4983f, 57.1165f, 55.3537f, 52.9257f, 52.8206f, 52.7151f, 52.6086f, 52.5008f, 52.3931f, 52.2848f, 52.1742f, 52.0642f, 51.9536f, 51.8411f, 51.7284f, 51.6149f, 51.5005f, 51.3854f, 51.2692f, 51.1249f, 53.6386f, 55.4615f, 56.8923f, 58.8876f, 60.5898f, 62.5807f, 64.2691f, 65.8243f, 67.3858f, 69.0955f, 71.1294f, 72.8869f, 74.9641f, 76.4691f, 78.4029f, 81.0916f, 81.1272f, 81.1645f, 81.2019f, 81.2393f, 81.2777f, 81.3156f, 81.355f, 81.3944f, 81.4329f, 81.4739f, 81.5146f, 81.5542f, 81.5957f, 81.6376f, 81.6787f ;
          :N_Anc_Filename = "N/A" ;
          :N_Aux_Filename = "VIIRS-CLD-AGG-LUT_npp_20020101010000Z_20020101010000Z_ee00000000000000Z_1_DEVL_ops_AFWA_ops.bin", "VIIRS-CEPS-EDR-DQTT_npp_20020101010000Z_20020101010000Z_ee00000000000000Z_1_noaa_ops_noaa_ops.xml", "VIIRS-GCE-AC_NPP_20020101010000Z_20020101010000Z_ee00000000000000Z_1_noaa_ops_noaa_ops.xml" ;
          :N_Beginning_Orbit_Number = 15 ;
          :N_Beginning_Time_IET = 1422175827996288 ;
          :N_Creation_Date = "20071024" ;
          :N_Creation_Time = "034009.709755Z" ;
          :N_Day_Night_Flag = "Day" ;
          :N_Ending_Time_IET = 1422175913449152 ;
          :N_Graceful_Degradation = "Yes" ;
          :N_Granule_ID = "NPP001212077949" ;
          :N_Granule_Version = "A1" ;
          :N_Input_Prod = "471ebd41-6baad-9b9dead7-e2c319ad", "471eb718-26a4a-9b9dead7-e2bec361", "471ebe86-e290b-9b9dead7-e2ca8930", "471ebe91-5fbf6-9b9dead7-e2c25c26", "471ebe8d-49cc4-9b9dead7-e2c0fcf0", "471ebe8e-2b2db-9b9dead7-e2bf1308", "471ebd44-b4fa1-9b9dead7-e2c7aea4", "471ebd4e-ad438-9b9dead7-e2c73345" ;
          :N_LEOA_Flag = "Off" ;
          :N_LUT_Version = "N/A" ;
          :N_NPOESS_Document_Ref = "TBD" ;
          :N_Nadir_Latitude_Max = 26.9418f ;
          :N_Nadir_Latitude_Min = 22.0234f ;
          :N_Nadir_Longitude_Max = 67.0874f ;
          :N_Nadir_Longitude_Min = 65.8319f ;
          :N_Percent_Erroneous_Data = 0.0f ;
          :N_Percent_Missing_Data = 0.0f ;
          :N_Percent_Not-Applicable_Data = 89.2686f ;
          :N_Reference_ID = "471ebe94-868a9-9b9dead7-e2c4c8dc" ;
          :N_Satellite_Local_Azimuth_Angle_Max = 179.998f ;
          :N_Satellite_Local_Azimuth_Angle_Min = -179.993f ;
          :N_Satellite_Local_Zenith_Angle_Max = 69.6511f ;
          :N_Satellite_Local_Zenith_Angle_Min = 0.019016f ;
          :N_Solar_Azimuth_Angle_Max = -141.781f ;
          :N_Solar_Azimuth_Angle_Min = -178.74f ;
          :N_Solar_Zenith_Angle_Max = 56.1325f ;
          :N_Solar_Zenith_Angle_Min = 38.2054f ;
          :N_Spacecraft_Maneuver = "Normal Operations" ;
          :North_Bounding_Coordinate = 28.5182f ;
          :South_Bounding_Coordinate = 19.1064f ;
          :West_Bounding_Coordinate = 51.1249f ;

        // group attributes:
      :N_Anc_Type_Tasked = "Official" ;
      :N_Collection_Short_Name = "VIIRS-CBH-EDR" ;
      :N_Dataset_Type_Tag = "EDR" ;
      :N_Instrument_Flight_SW_Version = 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ;
      :N_Processing_Domain = "OPS_SEG" ;
      :Operational_Mode = "NPP Science, VIIRS Operational" ;
    }
  }
}

$ h5dump -d /Data_Products/VIIRS-CBH-EDR/VIIRS-CBH-EDR_Aggr /home/all/testdata/netchdf/npp/VCBHO_npp_d20030125_t084955_e085121_b00015_c20071213022754_den_OPS_SEG.h5 > temp.txt

HDF5 "/home/all/testdata/netchdf/npp/VCBHO_npp_d20030125_t084955_e085121_b00015_c20071213022754_den_OPS_SEG.h5" {
DATASET "/Data_Products/VIIRS-CBH-EDR/VIIRS-CBH-EDR_Aggr" {
   DATATYPE  H5T_REFERENCE { H5T_STD_REF_OBJECT }
   DATASPACE  SIMPLE { ( 9 ) / ( 9 ) }
   DATA {
      DATASET "/home/all/testdata/netchdf/npp/VCBHO_npp_d20030125_t084955_e085121_b00015_c20071213022754_den_OPS_SEG.h5/All_Data/VIIRS-CBH-EDR_All/CloudBaseHeightLayer"
         DATA {
         (0,0,0): 65535, 65535, 65535, 65535,
         (0,1,0): 65535, 65535, 65535, 65535,
...
  DATASET "/home/all/testdata/netchdf/npp/VCBHO_npp_d20030125_t084955_e085121_b00015_c20071213022754_den_OPS_SEG.h5/All_Data/VIIRS-CBH-EDR_All/CloudBaseHeightTotal"
         DATA {
         (0,0): 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535,

So I think we should be returning an array of 9 strings with the contents as seen in "DATASET" above.
What we get though are just the last part of the names, not a full path:
    "CloudBaseHeightLayer", "CloudBaseHeightTotal", etc
*/

    @Test
    fun problemNPP() {
        val filename = testData + "netchdf/npp/VCBHO_npp_d20030125_t084955_e085121_b00015_c20071213022754_den_OPS_SEG.h5"
        compareCdlWithClib(filename, showCdl = true)
        readNetchdfData(filename, )
        compareDataWithClib(filename, )
    }

}