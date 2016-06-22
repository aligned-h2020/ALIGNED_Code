/*
Interlink Validation Tool
Author: Alan Meehanq
Date: 17 - June - 2016

Copyright 2016 Alan Meehan

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

class InterlinkValidator {

 //***********************************************************************
 //***********************************************************************
 //public static String path_to_isql = "/usr/local/virtuoso-opensource/bin/isql";
 //public static String dba_password = "dba";
 //public static String local_dataset_graph_name = "http://dbpedia.org";
 //public static String log_file_directory = "/home/meehanal/al_examples/interlink_validator/";
 //public static String rdf_log_file_directory = "/home/meehanal/al_examples/interlink_validator/";

 // Parameters used in the RDF log
 //public static String rdflog_source_dataset_uri = "http://downloads.dbpedia.org/2015-10";
 //public static String rdflog_source_dataset_distribution = "http://downloads.dbpedia.org/2015-10/core-i18n/en/";
 //************************************************************************
 //************************************************************************

/////////////////////////////////////////////////////////////////////////////////////////////////////

 public static String getTimeandDate(char status){

  String d = "";


  if(status == 'f'){
   DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ssZ");
   Date date = new Date();
   d = dateForm.format(date);
   d = d.replace("_" , "T");
  }
  else if(status == 's'){
   DateFormat dateForm = new SimpleDateFormat("yyyyMMdd_HHmmss");
   Date date = new Date();
   d = dateForm.format(date);
  }
  return d;
 }

/////////////////////////////////////////////////////////////////////////////////////////////////////

 public static String executeCommand(String command) {
  StringBuffer output = new StringBuffer();
  Process p;

  try {
   p = Runtime.getRuntime().exec(command);
   p.waitFor();
   BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
   String line = "";

   while ((line = reader.readLine())!= null){
    output.append(line);
   }
  }
  catch (Exception e){
   e.printStackTrace();
  }

  return output.toString();

 }

/////////////////////////////////////////////////////////////////////////////////////////////////////

 public static void downloadFileFromURL(String urlString, File destination) {
  try {
   URL website = new URL(urlString);
   ReadableByteChannel rbc;
   rbc = Channels.newChannel(website.openStream());
   FileOutputStream fos = new FileOutputStream(destination);
   fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
   fos.close();
   rbc.close();
  }
  catch (IOException e) {
   e.printStackTrace();
  }
 }
 
///////////////////////////////////////////////////////////////////////////////////////////////////////

 public static void main(String[] args){

  InterlinkValidator iv = new InterlinkValidator();

  // String to read in from iv_config.txt
  String iv_config = "";

  // confix variables /////////////////////////////////////////////////////
  String path_to_isql = "";
  String dba_password = "";
  String local_dataset_graph_name = "";
  String log_file_directory = "";
  String rdf_log_file_directory = "";

  String rdflog_source_dataset_uri = "";
  String rdflog_source_dataset_distribution = "";
  //////////////////////////////////////////////////////////////////////////

  // Read in from iv_config.txt
  try{
   FileInputStream iv_conf_in = new FileInputStream("iv_config.txt");
   DataInputStream conf_in = new DataInputStream(iv_conf_in);
   BufferedReader iv_conf_br = new BufferedReader(new InputStreamReader(conf_in));

   while( (iv_config = iv_conf_br.readLine() ) != null ){

    if(iv_config != null && !iv_config.isEmpty()){
     if(iv_config.charAt(0) != '#'){
      if(iv_config.charAt(0) != ' '){

       // 'path_to_isql' parameter
       if( iv_config.contains("p1=") ){
        int found_1 = iv_config.indexOf("p1=");
        if( iv_config.indexOf(' ', found_1) > 0 ){
         int found_2 = iv_config.indexOf(' ', found_1);
         path_to_isql = iv_config.substring(found_1+3, found_2);
        }
        else {
         path_to_isql = iv_config.substring(found_1+3);
        }
       }

       // 'dba_password' parameter
       else if( iv_config.contains("p2=") ){
        int found_1 = iv_config.indexOf("p2=");
        if( iv_config.indexOf(' ', found_1) > 0 ){
         int found_2 = iv_config.indexOf(' ', found_1);
         dba_password = iv_config.substring(found_1+3, found_2);
        }
        else {
         dba_password = iv_config.substring(found_1+3);
        }
       }

       // 'local_dataset_graph_name' parameter
       else if( iv_config.contains("p3=") ){
        int found_1 = iv_config.indexOf("p3=");
        if( iv_config.indexOf(' ', found_1) > 0 ){
         int found_2 = iv_config.indexOf(' ', found_1);
         local_dataset_graph_name = iv_config.substring(found_1+3, found_2);
        }
        else {
         local_dataset_graph_name = iv_config.substring(found_1+3);
        }
       }

       // 'log_file_directory' parameter
       else if( iv_config.contains("p4=") ){
        int found_1 = iv_config.indexOf("p4=");
        if( iv_config.indexOf(' ', found_1) > 0 ){
         int found_2 = iv_config.indexOf(' ', found_1);
         log_file_directory = iv_config.substring(found_1+3, found_2);
        }
        else {
         log_file_directory = iv_config.substring(found_1+3);
        }
       }

       // 'rdf_log_file_directory' parameter
       else if( iv_config.contains("p5=") ){
        int found_1 = iv_config.indexOf("p5=");
        if( iv_config.indexOf(' ', found_1) > 0 ){
         int found_2 = iv_config.indexOf(' ', found_1);
         rdf_log_file_directory = iv_config.substring(found_1+3, found_2);
        }
        else {
         rdf_log_file_directory = iv_config.substring(found_1+3);
        }
       }

       // 'rdflog_source_dataset_uri' parameter
       else if( iv_config.contains("p6=") ){
        int found_1 = iv_config.indexOf("p6=");
        if( iv_config.indexOf(' ', found_1) > 0 ){
         int found_2 = iv_config.indexOf(' ', found_1);
         rdflog_source_dataset_uri = iv_config.substring(found_1+3, found_2);
        }
        else {
         rdflog_source_dataset_uri = iv_config.substring(found_1+3);
        }
       }

       // 'rdflog_source_dataset_distribution' parameter
       else if( iv_config.contains("p7=") ){
        int found_1 = iv_config.indexOf("p7=");
        if( iv_config.indexOf(' ', found_1) > 0 ){
         int found_2 = iv_config.indexOf(' ', found_1);
         rdflog_source_dataset_distribution = iv_config.substring(found_1+3, found_2);
        }
        else {
         rdflog_source_dataset_distribution = iv_config.substring(found_1+3);
        }
       }

      }
     }
    }

   }
  // System.out.println("p1="+path_to_isql+"\np2="+dba_password+"\np3="+local_dataset_graph_name+"\np4="+log_file_directory+"\np5="+rdf_log_file_directory+"\np6="+rdflog_source_dataset_uri+"\np7="+rdflog_source_dataset_distribution );

  }
  catch (Exception e){
   System.err.println("ERROR: " + e.getMessage());
  }

  // Some Error handeling of config parameters
  //p1
  if(path_to_isql.equals("") || path_to_isql.equals(" ")){
   System.out.println("\nERROR: In 'iv_config.txt' p1 not declared\n");
   System.exit(1);
  }
  if( path_to_isql.charAt(0) == '/' ){}
  else {
   System.out.println("\nERROR: In 'iv_config.txt' p1 must be a 'file path' and begin with a '/'\n");
   System.exit(1);
  }

  //p2
  if(dba_password.equals("") || dba_password.equals(" ")){
   System.out.println("\nERROR: In 'iv_config.txt' p2 not declared\n");
   System.exit(1);
  }

  //p3
  if(local_dataset_graph_name.equals("") || local_dataset_graph_name.equals(" ")){
   System.out.println("\nERROR: In 'iv_config.txt' p3 not declared\n");
   System.exit(1);
  }
  if( local_dataset_graph_name.charAt(0) == 'h' && local_dataset_graph_name.charAt(1) == 't' ){}
  else {
   System.out.println("\nERROR: In 'iv_config.txt' p3 must be a URL and begin with 'http'\n");
   System.exit(1);
  }

  //p4
  if(log_file_directory.equals("") || log_file_directory.equals(" ")){
   System.out.println("\nERROR: In 'iv_config.txt' p4 not declared\n");
   System.exit(1);
  }
  if( log_file_directory.charAt(0) == '/' ){}
  else {
   System.out.println("\nERROR: In 'iv_config.txt' p4 must be a 'file path' and begin with a '/'\n");
   System.exit(1);
  }

  //p5
  if(rdf_log_file_directory.equals("") || rdf_log_file_directory.equals(" ")){
   System.out.println("\nERROR: In 'iv_config.txt' p5 not declared\n");
   System.exit(1);
  }
  if( rdf_log_file_directory.charAt(0) == '/' ){}
  else {
   System.out.println("\nERROR: In 'iv_config.txt' p6 must be a 'file path' and begin with a '/'\n");
   System.exit(1);
  }

  //p6
  if(rdflog_source_dataset_uri.equals("") || rdflog_source_dataset_uri.equals(" ")){
   System.out.println("\nERROR: In 'iv_config.txt' p6 not declared\n");
   System.exit(1);
  }
  if( rdflog_source_dataset_uri.charAt(0) == 'h' && rdflog_source_dataset_uri.charAt(1) == 't' ){}
  else {
   System.out.println("\nERROR: In 'iv_config.txt' p6 must be a URL and begin with 'http'\n");
   System.exit(1);
  }

  //p7
  if(rdflog_source_dataset_distribution.equals("") || rdflog_source_dataset_distribution.equals(" ")){
   System.out.println("\nERROR: In 'iv_config.txt' p7 not declared\n");
   System.exit(1);
  }
  if( rdflog_source_dataset_distribution.charAt(0) == 'h' && rdflog_source_dataset_distribution.charAt(1) == 't' ){}
  else {
   System.out.println("\nERROR: In 'iv_config.txt' p7 must be a URL and begin with 'http'\n");
   System.exit(1);
  }


  ////////////////////////////////////////////////////////////////////////////////

  String date_time = getTimeandDate('s');

  String log_file_name = log_file_directory + "iv_log_"+date_time+".txt";
  String rdf_log_file_name = rdf_log_file_directory + "rdf_iv_log_"+date_time+".nt";

  String command_1 = "";
  String command_2 = "";
  String command_3_s = "";
  String command_3_e = "";
  String sparql_query_1 = "SELECT DISTINCT ?a ?p ?o WHERE { GRAPH <http://al_ex#g1> {?a ?p ?o . FILTER NOT EXISTS { GRAPH <"+local_dataset_graph_name+"> {?a ?pp ?oo } } } }";
  String sparql_query_2 = "";

  //Blank node for use in RDF Log File
  int blank_node = 0;

  //Open Log File
  File log_file = new File(log_file_name);

  //Open RDF Log File
  File rdf_log_file = new File(rdf_log_file_name);

  // String to read in from external_datasets.txt
  String ext_dataset = "";

  boolean error = false;

  ////////////////////////////////////////////////////////////////////////////////////

  try{
   FileInputStream ext_ds_in = new FileInputStream("external_datasets.txt");
   DataInputStream in = new DataInputStream(ext_ds_in);
   BufferedReader br = new BufferedReader(new InputStreamReader(in));

   BufferedWriter log_writer = new BufferedWriter(new FileWriter(log_file));
   BufferedWriter rdf_log_writer = new BufferedWriter(new FileWriter(rdf_log_file));

   //Writing prefixes to RDF log file
   rdf_log_writer.write("@prefix ia: <http://aligned-project.eu/interlink-analysis#> .\n@prefix dlo: <http://aligned-project.eu/ontologies/dlo> .\n@prefix dataid: <http://dataid.dbpedia.org/ns/core#> .\n@prefix dcat: <http://www.w3.org/ns/dcat#> .\n@prefix prov: <http://www.w3.org/ns/prov#> .\n@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n\n");


   while((ext_dataset = br.readLine()) != null){
    error = false;

    if(ext_dataset != null && !ext_dataset.isEmpty()){
     if(ext_dataset.charAt(0) != '#'){
      if(ext_dataset.charAt(0) != ' '){

       String param_1 = "", param_2 = "", param_2_temp = "", param_3 = "", param_4 = "", param_4_temp = "", param_5 = "", param_6 = "";
       boolean param_6_bool = false;

       //--------------------------------------------------------------------------------------------------------------------------------------------
       // Parsing the parameters from external_datasets.txt file-
       //p1
       if( ext_dataset.contains("p1=") ){
        int found_1 = ext_dataset.indexOf("p1=");
        int found_2 = ext_dataset.indexOf(' ', found_1);
        param_1 = ext_dataset.substring(found_1+3, found_2);
       }
       //p2
       if( ext_dataset.contains("p2=") ){
        int found_1 = ext_dataset.indexOf("p2=");
        int found_2 = ext_dataset.indexOf(' ', found_1);
        param_2 = ext_dataset.substring(found_1+3, found_2);
       }
       //p3
       if( ext_dataset.contains("p3=") ){
        int found_1 = ext_dataset.indexOf("p3=");
        if( ext_dataset.indexOf(' ', found_1) > 0 ){
         int found_2 = ext_dataset.indexOf(' ', found_1);
         param_3 = ext_dataset.substring(found_1+3, found_2);
        }
        else {
         param_3 = ext_dataset.substring(found_1+3);
        }
       }
       //p4
       if( ext_dataset.contains("p4=") ){
        int found_1 = ext_dataset.indexOf("p4=");
        if( ext_dataset.indexOf(' ', found_1) > 0 ){
         int found_2 = ext_dataset.indexOf(' ', found_1);
         param_4 = ext_dataset.substring(found_1+3, found_2);
        }
        else {
         param_4 = ext_dataset.substring(found_1+3);
        }
       }
       //p5
       if( ext_dataset.contains("p5=") ){
        int found_1 = ext_dataset.indexOf("p5=");
        if( ext_dataset.indexOf(' ', found_1) > 0 ){
         int found_2 = ext_dataset.indexOf(' ', found_1);
         param_5 = ext_dataset.substring(found_1+3, found_2);
        }
        else {
         param_5 = ext_dataset.substring(found_1+3);
        }
       }
       //p6
       if( ext_dataset.contains("p6=") ){
        int found_1 = ext_dataset.indexOf("p6=");
        if( ext_dataset.indexOf(' ', found_1) > 0 ){
         int found_2 = ext_dataset.indexOf(' ', found_1);
         param_6 = ext_dataset.substring(found_1+3, found_2);
        }
        else {
         param_6 = ext_dataset.substring(found_1+3);
        }
        param_6_bool = true;
       }

       //System.out.print("\nparam1: "+param_1+"\nparam2: "+param_2+"\nparam3: "+param_3+"\nparam4: "+param_4+"\nparam5: "+param_5+"\nparam6: "+param_6+"\n\n");
       //--------------------------------------------------------------------------------------------------------------------------------------------

       //--------------------------------------------------------------------------------------------------------------------------------------------
       // Some Error handeling of external dataset parameters
       if(param_1.equals("") || param_1.equals(" ")){
        System.out.println("\nERROR: p1 not declared\n");
        error = true;
       }

       if(param_2.equals("") || param_2.equals(" ")){
        System.out.println("\nERROR: For '"+param_1+"', p2 not declared\n");
        error = true;
       }
       if( param_2.charAt(0) == '/' || (param_2.charAt(0) == 'h' && param_2.charAt(1) == 't') ){}
       else {
        System.out.println("\nERROR: For '"+param_1+"', p2 must be a 'path to file' and begin with a '/' or it must be a URL and begin with 'http'\n");
        error = true;
       }

       if(param_3.equals("") || param_3.equals(" ")){
        System.out.println("\nERROR: For '"+param_1+"', p3 not declared\n");
        error = true;
       }

       if(param_6_bool == true){
        if(param_6.equals("") || param_6.equals(" ")){
         System.out.println("\nERROR: For '"+param_1+"', p6 not declared\n");
         error = true;
        }
        else if(param_6.charAt(0) != 'h' && param_6.charAt(1) != 't'){
          System.out.println("\nERROR: For '"+param_1+"', p6 must be a 'URI' and begin with a 'http'\n");
          error = true;
        }
       }

       if(error == false){
        if(param_3.equals("F") || param_3.equals("FG") || param_3.equals("G") || param_3.equals("D") || param_3.equals("N") ){}
        else {
         System.out.println("\nERROR: For '"+param_1+"', p3 is not one of the following: 'F', 'FG', 'G', 'D' or 'N'\n");
         error = true;
        }

        if(error == false){
         if(param_3.equals("F") || param_3.equals("G") || param_3.equals("FG") || param_3.equals("D")){
          if(param_4.equals("") || param_4.equals(" ")){
           System.out.println("\nERROR: For '"+param_1+"', p4 not declared\n");
           error = true;
          }

          if(param_3.equals("F") || param_3.equals("G") || param_3.equals("FG") ){
           if(param_4.charAt(0) != 'h' && param_4.charAt(1) != 't'){
            System.out.println("\nERROR: For '"+param_1+"', p4 must be a 'URI' and begin with a 'http'\n");
            error = true;
           }
          }

          if(param_3.equals("D")){
           if( param_4.charAt(0) == '/' || (param_4.charAt(0) == 'h' && param_4.charAt(1) == 't') ){}
           else {
            System.out.println("\nERROR: For '"+param_1+"', p4 must be a 'path to file' and begin with a '/' or it must be a URL and begin with 'http' \n");
            error = true;
           }
          }

          if(param_3.equals("FG")){
           if(param_5.equals("") || param_5.equals(" ")){
            System.out.println("\nERROR: For '"+param_1+"', p5 not declared\n");
            error = true;
           }
           else if( param_5.charAt(0) == 'h' && param_5.charAt(1) == 't' ){}
           else {
            System.out.println("\nERROR: For '"+param_1+"', p5 must be a URL and begin with 'http' \n");
            error = true;;
           }
          }
         }
        }
       }
       //--------------------------------------------------------------------------------------------------------------------------------------------

       //--------------------------------------------------------------------------------------------------------------------------------------------
       // Handeling Downloaded Files
       if(param_2.charAt(0) == 'h' && param_2.charAt(1) == 't'){

        String temp_1 = param_2.substring( param_2.lastIndexOf('/') + 1 );
        File file_temp_1 = new File("temp/"+temp_1);

        downloadFileFromURL(param_2, file_temp_1);

        String temp_2 = temp_1.substring( temp_1.lastIndexOf('.') + 1 );

        if( temp_2.equals("bz2") ){
         String[] cmd111 = {"/bin/sh" , "-c" , "bzip2 -d temp/"+temp_1};
         try {
          Process p111 = Runtime.getRuntime().exec(cmd111);
          p111.waitFor();
         }
         catch (Exception e){
          e.printStackTrace();
         }
         param_2_temp = System.getProperty("user.dir")+"/temp/"+temp_1.substring(0, temp_1.lastIndexOf('.') );
        }
        else if( temp_2.equals("nt") ){
         param_2_temp = System.getProperty("user.dir")+"/temp/"+temp_1;
        }
        else if( temp_2.equals("ttl") ){
         param_2_temp = System.getProperty("user.dir")+"/temp/"+temp_1;
        }
        else {
          System.out.println("\nERROR: For '"+param_1+"', p2 file extension not recognized, file must end with ',ttl', '.nt' or '.nt.bz2'\n");
          error = true;
        }

       }

       /////////////////////////////////////////////////////////////////////////////////////////

       if(param_3.equals("D")){

        if(param_4.charAt(0) == 'h' && param_4.charAt(1) == 't'){

         String temp_1 = param_4.substring( param_4.lastIndexOf('/') + 1 );
         File file_temp_1 = new File("temp/"+temp_1);

         downloadFileFromURL(param_4, file_temp_1);

         String temp_2 = temp_1.substring( temp_1.lastIndexOf('.') + 1 );

         if( temp_2.equals("bz2") ){
          String[] cmd111 = {"/bin/sh" , "-c" , "bzip2 -d temp/"+temp_1};
          try {
           Process p111 = Runtime.getRuntime().exec(cmd111);
           p111.waitFor();
          }
          catch (Exception e){
           e.printStackTrace();
          }
          param_4_temp = System.getProperty("user.dir")+"/temp/"+temp_1.substring(0, temp_1.lastIndexOf('.') );
         }
         else if( temp_2.equals("nt") ){
          param_4_temp = System.getProperty("user.dir")+"/temp/"+temp_1;
         }
         else if( temp_2.equals("ttl") ){
          param_4_temp = System.getProperty("user.dir")+"/temp/"+temp_1;
         }
         else {
          System.out.println("\nERROR: For '"+param_1+"', p4 file extension not recognized, file must end with '.ttl', '.nt' or '.nt.bz2'\n");
          error = true;
         }
        }
       }

       //---------------------------------------------------------------------------------------------------------------------------------------------

       //---------------------------------------------------------------------------------------------------------------------------------------------
       // Query + execution command Generator
       if(error == false){
        // Federated SPARQL query to external dataset
        if(param_3.equals("F") || param_3.equals("FG")){
         if(param_3.equals("FG")) {
          sparql_query_2 = "SELECT DISTINCT ?result ?ss ?pp ?a WHERE { GRAPH <http://al_ex#g1> { ?ss ?pp ?a } SERVICE <"+param_4+"> { GRAPH <"+param_5+"> { OPTIONAL {?a ?p ?o } } } BIND ( IF( bound(?p) , 'Valid', 'Invalid') AS ?result) }";
         }
         else {
          sparql_query_2 = "SELECT DISTINCT ?result ?ss ?pp ?a   WHERE {  GRAPH <http://al_ex#g1> { ?ss ?pp ?a } SERVICE <"+param_4+"> {  OPTIONAL {?a ?p ?o } } BIND ( IF( bound(?p) , 'Valid', 'Invalid') AS ?result) }";
         }
        }

        // External dataset in named graph in triple-store
        else if(param_3.equals("G")){
         sparql_query_2 = "SELECT DISTINCT ?s ?p ?a WHERE { GRAPH <http://al_ex#g1> {?s ?p ?a . FILTER NOT EXISTS { GRAPH <"+param_4+"> {?a ?pp ?oo } } } }";
        }

        // External dataset in data dump file
        else if(param_3.equals("D")){
         sparql_query_2 = "SELECT DISTINCT ?s ?p ?a WHERE { GRAPH <http://al_ex#g1> {?s ?p ?a . FILTER NOT EXISTS { GRAPH <http://al_ex#g2> {?a ?pp ?oo } } } }";
         command_3_s = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nDB.DBA.TTLP_MT (file_to_string_output ('"+param_4_temp+"'), '', 'http://al_ex#g2');\nEOF";
         command_3_e = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nSPARQL CLEAR GRAPH <http://al_ex#g1>;\nSPARQL CLEAR GRAPH <http://al_ex#g2>;\nEOF";
        }

        // No external dataset specified
        else if(param_3.equals("N")){
         command_3_e = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nSPARQL CLEAR GRAPH <http://al_ex#g1>;\nEOF";
        }

        command_1 = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nDB.DBA.TTLP_MT (file_to_string_output ('"+param_2_temp+"'), '', 'http://al_ex#g1');\nSPARQL "+sparql_query_1+";\nEOF";
        command_2 = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nSPARQL "+sparql_query_2+";\nEOF";
       }
       //---------------------------------------------------------------------------------------------------------------------------------------------


       //if(error == false){
       StringBuffer output = new StringBuffer();

       ///// Interlink Valid and Invalid File Setup
       File temp_il_file = new File("temp_il.nt");
       BufferedWriter temp_il_writer = new BufferedWriter(new FileWriter(temp_il_file));

       File valid_file = new File("valid/"+param_1+"_valid_"+date_time+".nt");
       BufferedWriter valid_il_writer = new BufferedWriter(new FileWriter(valid_file));

       File invalid_file = new File("invalid/"+param_1+"_invalid_"+date_time+".nt");
       BufferedWriter invalid_il_writer = new BufferedWriter(new FileWriter(invalid_file));


       //Writing to RDF log file
       String p2_tmp = "";
       String p4_tmp = "";

       if(param_2.charAt(0) == 'h' && param_2.charAt(1) == 't'){
        p2_tmp = "<"+param_2+">";
       }
       else {
        p2_tmp = "<file://"+param_2+">";
       }

       if(param_3.equals("F") || param_3.equals("FG") || param_3.equals("D") || param_3.equals("G") ){
        if(param_4.charAt(0) == 'h' && param_4.charAt(1) == 't'){
         p4_tmp = "<"+param_4+">";
        }
        else {
         p4_tmp = "<file://"+param_4+">";
        }

        rdf_log_writer.write("_:b"+(++blank_node)+"  a  dlo:InterlinkValidation ;\n     dlo:isSupportedBy  <http://aligned-project.eu/tools#InterlinkValidationTool> ;\n     dlo:consumes  <"+rdflog_source_dataset_distribution+"> ;\n     dlo:consumes  "+p2_tmp+" ;\n     dlo:consumes  "+p4_tmp+" ;\n     dlo:produces _:b"+(++blank_node)+" .\n\n<"+rdflog_source_dataset_uri+">  a  dataid:Dataset ;\n     dataid:distribution  <"+rdflog_source_dataset_distribution+"> .\n\n");

        if(param_6_bool == true){
         rdf_log_writer.write("<"+param_6+">  a  dataid:Dataset ;\n     dataid:distribution  "+p4_tmp+" .\n\n");
        }

        rdf_log_writer.write(p2_tmp+"  a  dlo:DataEntity .\n\n");

        rdf_log_writer.write("<"+rdflog_source_dataset_distribution+">  a  dataid:Distribution ;\n     dataid:distributionOf  <"+rdflog_source_dataset_uri+"> ;\n     dcat:accessURL  <"+rdflog_source_dataset_distribution+"> .\n\n");

        rdf_log_writer.write(p4_tmp+"  a  dataid:Distribution ;\n      ");

        if(param_6_bool == true){
         rdf_log_writer.write("dataid:distributionOf  <"+param_6+"> ;\n     ");
        }

        rdf_log_writer.write("dcat:accessURL  "+p4_tmp+" .\n\n");

       }

       ////////////////////////////
       else if(param_3.equals("N")){
        rdf_log_writer.write("_:b"+(++blank_node)+"  a  dlo:InterlinkValidation ;\n     dlo:isSupportedBy  <http://aligned-project.eu/tools#InterlinkValidationTool> ;\n     dlo:consumes  <"+rdflog_source_dataset_distribution+"> ;\n     dlo:consumes  "+p2_tmp+" ;\n     dlo:produces _:b"+(++blank_node)+" .\n\n<"+rdflog_source_dataset_uri+">  a  dataid:Dataset ;\n     dataid:distribution  <"+rdflog_source_dataset_distribution+"> .\n\n");

        if(param_6_bool == true){
         rdf_log_writer.write("<"+param_6+">  a  dataid:Dataset .\n\n");
        }

        rdf_log_writer.write(p2_tmp+"  a  dlo:DataEntity .\n\n");

        rdf_log_writer.write("<"+rdflog_source_dataset_distribution+">  a  dataid:Distribution ;\n     dataid:distributionOf  <"+rdflog_source_dataset_uri+"> ;\n     dcat:accessURL  <"+rdflog_source_dataset_distribution+"> .\n\n");

       }

       //---------------------------------------------------------------------------------------------------------------------------------------------
       // Executing - where param_3 = "D"
       if(error == false && param_3.equals("D")){

        int count = 0;
        String sparql_count_command = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nSPARQL SELECT DISTINCT (count(?s) AS ?count) WHERE { GRAPH <http://al_ex#g1> { ?s ?p ?o } };\nEOF";
        String total = "";

        //Execute command_1
        String[] cmd1 = {"/bin/sh" , "-c" , command_1 };
        try {
         Process p1 = Runtime.getRuntime().exec(cmd1);
         p1.waitFor();
         BufferedReader reader1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
         String line1 = "";

         while ((line1 = reader1.readLine())!= null){
          if( !line1.equals("") ){
           if(line1.charAt(0) == 'h' && line1.charAt(1) == 't' ){
            output.append( line1 + "\n");
            //System.out.println(line1);
            count++;
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }


        int it = 1;
        //Execute sparql_count_command
        String[] cmd2 = {"/bin/sh" , "-c" , sparql_count_command };
        try {
         Process p2 = Runtime.getRuntime().exec(cmd2);
         p2.waitFor();
         BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
         String line2 = "";

         while ((line2 = reader2.readLine())!= null){
          if( it == 9){
           total = line2;
           //System.out.println(line2);
          }
          it++;
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }


        log_writer.write("\nInterLink Validation Tool Log - Run at time: "+getTimeandDate('f')+"\nInterlinks Validated between DBpedia and "+param_1+"\nTotal number of interlinks checked: "+total+" \n\n");
        log_writer.write("Invald interlinks due to DBpedia resources:\n"+output.toString()+"\nNumber of invalid interlinks due to DBpedia resources: "+count+"\n");
        if(count > 0)
         log_writer.write("Invalidation Reason: DBpedia resource identifiers do not exist in DBpedia dataset\n\n");
        else
         log_writer.write("\n\n");


        // RDF Log
        rdf_log_writer.write("_:b"+blank_node+"  a  dlo:InterlinkValidationReport ;\n     dlo:wasProducedBy  _:b"+(blank_node-1)+" ;\n     ");

        if( !output.toString().equals("") ){
         String[] rdf_temp_output_1 = output.toString().split("\n");
         int cnt_tmp1 = rdf_temp_output_1.length;
         String[][] rdf_temp_output_2 = new String[cnt_tmp1][];

         for(int i = 0; i < cnt_tmp1; i++){
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replace(" ", "");
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replace("http://", " http://");
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replaceFirst(" ", "");
          rdf_temp_output_2[i] = rdf_temp_output_1[i].split(" ");

          rdf_log_writer.write("ia:invalidInterlink  [  rdf:subject  <"+rdf_temp_output_2[i][0]+"> ;\n                             rdf:predicate  <"+rdf_temp_output_2[i][1]+"> ;\n                             rdf:object  <"+rdf_temp_output_2[i][2]+"> ] ;\n     ");
         }
        }

        /////////////////////////////////////////////////////////////////////
        String temp_output = output.toString();

        temp_output = temp_output.replace(" ", "");
        temp_output = temp_output.replace("http://", "> <http://");
        temp_output = temp_output.replace("\n", ">.\n");
        temp_output = temp_output.replace("\n> <", "\n<");
        temp_output = temp_output.replaceFirst("> <" , "<");

        temp_il_writer.write(temp_output);
        //temp_il_writer.close();
        //////////////////////////////////////////////////////////////////////


        output = new StringBuffer();
        count = 0;

        //Execute command_3_s
        String[] cmd3 = {"/bin/sh" , "-c" , command_3_s };
        try {
         Process p3 = Runtime.getRuntime().exec(cmd3);
         p3.waitFor();
         BufferedReader reader3 = new BufferedReader(new InputStreamReader(p3.getInputStream()));
         String line3 = "";

          while ((line3 = reader3.readLine())!= null){
           //System.out.println(line3);
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }


        //Execute command_2
        String[] cmd4 = {"/bin/sh" , "-c" , command_2 };
        try {
         Process p4 = Runtime.getRuntime().exec(cmd4);
         //p4.waitFor();
         BufferedReader reader4 = new BufferedReader(new InputStreamReader(p4.getInputStream()));
         String line4 = "";

         while ((line4 = reader4.readLine())!= null){
          if( !line4.equals("") ){
           if(line4.charAt(0) == 'h' && line4.charAt(1) == 't' ){
            output.append(line4 + "\n");
            //System.out.println(line4);
            count++;
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }
		
        log_writer.write("Invalid interlinks due to "+param_1+" resources:\n"+output.toString()+"\nNumber of invalid interlinks due to "+param_1+" resources: "+count+"\n");
        if(count > 0)
         log_writer.write("Invalidation Reason: "+param_1+" resource identifiers do not exist in "+param_1+" dataset\n");
        log_writer.write("\n-----------------------------------------------------------------------------------------------\n");


        // RDF Log
        if( !output.toString().equals("") ){
         String[] rdf_temp_output_1 = output.toString().split("\n");
         int cnt_tmp1 = rdf_temp_output_1.length;
         String[][] rdf_temp_output_2 = new String[cnt_tmp1][];

         for(int i = 0; i < cnt_tmp1; i++){
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replace(" ", "");
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replace("http://", " http://");
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replaceFirst(" ", "");
          rdf_temp_output_2[i] = rdf_temp_output_1[i].split(" ");

          rdf_log_writer.write("ia:invalidInterlink  [  rdf:subject  <"+rdf_temp_output_2[i][0]+"> ;\n                             rdf:predicate  <"+rdf_temp_output_2[i][1]+"> ;\n                             rdf:object  <"+rdf_temp_output_2[i][2]+"> ] ;\n     ");
         }
        }

        rdf_log_writer.write("prov:generatedAtTime \""+getTimeandDate('f')+"\"^^xsd:dateTime .\n\n\n");

        ////////////////////////////////////////////////////////////////////////
        temp_output = "";
        temp_output = output.toString();

        String temp_il_path = executeCommand("pwd")+"/temp_il.nt";

        temp_output = temp_output.replace(" ", "");
        temp_output = temp_output.replace("http://", "> <http://");
        temp_output = temp_output.replace("\n", ">.\n");
        temp_output = temp_output.replace("\n> <", "\n<");
        temp_output = temp_output.replaceFirst("> <" , "<");

        temp_il_writer.write(temp_output);
        temp_il_writer.close();

        String sparql_temp_valid = "SELECT ?s ?p ?o WHERE { GRAPH <http://al_ex#g1> {?s ?p ?o. FILTER NOT EXISTS { GRAPH <http://al_ex#g3> { ?s ?p ?o  }  } } }";
        String command_temp_1 = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nDB.DBA.TTLP_MT (file_to_string_output ('"+temp_il_path+"'), '', 'http://al_ex#g3');\nSPARQL "+sparql_temp_valid+";\nEOF";

        StringBuffer temp_output_1 = new StringBuffer();
        String[] cmd_temp_1 = {"/bin/sh" , "-c" , command_temp_1 };
        try {
         Process t1 = Runtime.getRuntime().exec(cmd_temp_1);
         //t1.waitFor();
         BufferedReader reader_temp_1 = new BufferedReader(new InputStreamReader(t1.getInputStream()));
         String line_temp_1 = "";

         while ((line_temp_1 = reader_temp_1.readLine())!= null){
          if( !line_temp_1.equals("") ){
           if(line_temp_1.charAt(0) == 'h' && line_temp_1.charAt(1) == 't' ){
            temp_output_1.append( line_temp_1 + "\n");
            //System.out.println(line_temp_1);
            //count++;
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

        temp_il_file.delete();

        String temp_output_11 = temp_output_1.toString();
        temp_output_11 = temp_output_11.replace(" ", "");
        temp_output_11 = temp_output_11.replace("http://", "> <http://");
        temp_output_11 = temp_output_11.replace("\n", ">.\n");
        temp_output_11 = temp_output_11.replace("\n> <", "\n<");
        temp_output_11 = temp_output_11.replaceFirst("> <" , "<");

        valid_il_writer.write(temp_output_11);
        valid_il_writer.close();

        //////////

        String sparql_temp_invalid = "SELECT ?s ?p ?o WHERE { GRAPH <http://al_ex#g1> {?s ?p ?o. FILTER EXISTS { GRAPH <http://al_ex#g3> { ?s ?p ?o  }  } } }";
        String command_temp_2 = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nSPARQL "+sparql_temp_invalid+";\n SPARQL CLEAR GRAPH <http://al_ex#g3>;\nEOF";

        StringBuffer temp_output_2 = new StringBuffer();
        String[] cmd_temp_2 = {"/bin/sh" , "-c" , command_temp_2 };
        try {
         Process t2 = Runtime.getRuntime().exec(cmd_temp_2);
         //t1.waitFor();
         BufferedReader reader_temp_2 = new BufferedReader(new InputStreamReader(t2.getInputStream()));
         String line_temp_2 = "";

         while ((line_temp_2 = reader_temp_2.readLine())!= null){
          if( !line_temp_2.equals("") ){
           if(line_temp_2.charAt(0) == 'h' && line_temp_2.charAt(1) == 't' ){
            temp_output_2.append( line_temp_2 + "\n");
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

        String temp_output_22 = temp_output_2.toString();
        temp_output_22 = temp_output_22.replace(" ", "");
        temp_output_22 = temp_output_22.replace("http://", "> <http://");
        temp_output_22 = temp_output_22.replace("\n", ">.\n");
        temp_output_22 = temp_output_22.replace("\n> <", "\n<");
        temp_output_22 = temp_output_22.replaceFirst("> <" , "<");

        invalid_il_writer.write(temp_output_22);
        invalid_il_writer.close();

        /////////////////////////////////////////////////////////////////////////

        //Execute command_3_e
        String[] cmd5 = {"/bin/sh" , "-c" , command_3_e };
        try {
         Process p5 = Runtime.getRuntime().exec(cmd5);
         p5.waitFor();
         BufferedReader reader5 = new BufferedReader(new InputStreamReader(p5.getInputStream()));
         String line5 = "";

         while ((line5 = reader5.readLine())!= null){
          //System.out.println(line5);
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

       }
       //---------------------------------------------------------------------------------------------------------------------------------------------

       //---------------------------------------------------------------------------------------------------------------------------------------------
       //  Executing - where param_3 = "N"
       else if(error == false && param_3.equals("N")){

        int count = 0;
        String sparql_count_command = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nSPARQL SELECT DISTINCT (count(?s) AS ?count) WHERE { GRAPH <http://al_ex#g1> { ?s ?p ?o } };\nEOF";
        String total = "";

        //Execute command_1
        String[] cmd1 = {"/bin/sh" , "-c" , command_1 };
        try {
         Process p1 = Runtime.getRuntime().exec(cmd1);
         p1.waitFor();
         BufferedReader reader1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
         String line1 = "";

         while ((line1 = reader1.readLine())!= null){
          if( !line1.equals("") ){
           if(line1.charAt(0) == 'h' && line1.charAt(1) == 't' ){
            output.append( line1 + "\n");
            //System.out.println(line1);
            count++;
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

        int it = 1;
        //Execute sparql_count_command
        String[] cmd2 = {"/bin/sh" , "-c" , sparql_count_command };
        try {
         //Thread.sleep(2000);
         Process p2 = Runtime.getRuntime().exec(cmd2);
         p2.waitFor();
         BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
         String line2 = "";

         while ((line2 = reader2.readLine())!= null){
          if( it == 9){
           total = line2;
           //System.out.println(line2);
          }
          it++;
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }


        log_writer.write("\nInterLink Validation Tool Log - Run at time: "+getTimeandDate('f')+"\nInterlinks Validated between DBpedia and "+param_1+"\nTotal number of interlinks checked: "+total+" \n\n");
        log_writer.write("Invald interlinks due to DBpedia resources:\n"+output.toString()+"\nNumber of invalid interlinks due to DBpedia resources: "+count+"\n");
        if(count > 0)
         log_writer.write("Invalidation Reason: DBpedia resource identifiers do not exist in DBpedia dataset\n\n");
        log_writer.write("\n------------------------------------------------------------------------------------\n");

        // RDF Log
        rdf_log_writer.write("_:b"+blank_node+"  a  dlo:InterlinkValidationReport ;\n     dlo:wasProducedBy  _:b"+(blank_node-1)+" ;\n     ");

        if( !output.toString().equals("") ){
         String[] rdf_temp_output_1 = output.toString().split("\n");
         int cnt_tmp1 = rdf_temp_output_1.length;
         String[][] rdf_temp_output_2 = new String[cnt_tmp1][];

         for(int i = 0; i < cnt_tmp1; i++){
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replace(" ", "");
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replace("http://", " http://");
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replaceFirst(" ", "");
          rdf_temp_output_2[i] = rdf_temp_output_1[i].split(" ");

          rdf_log_writer.write("ia:invalidInterlink  [  rdf:subject  <"+rdf_temp_output_2[i][0]+"> ;\n                             rdf:predicate  <"+rdf_temp_output_2[i][1]+"> ;\n                             rdf:object  <"+rdf_temp_output_2[i][2]+"> ] ;\n     ");
         }
        }

        rdf_log_writer.write("prov:generatedAtTime \""+getTimeandDate('f')+"\"^^xsd:dateTime .\n\n\n");

        /////////////////////////////////////////////////////////////////////////////
        String temp_output = output.toString();

        String temp_il_path = executeCommand("pwd")+"/temp_il.nt";

        temp_output = temp_output.replace(" ", "");
        temp_output = temp_output.replace("http://", "> <http://");
        temp_output = temp_output.replace("\n", ">.\n");
        temp_output = temp_output.replace("\n> <", "\n<");
        temp_output = temp_output.replaceFirst("> <" , "<");

        temp_il_writer.write(temp_output);
        temp_il_writer.close();

        String sparql_temp_valid = "SELECT ?s ?p ?o WHERE { GRAPH <http://al_ex#g1> {?s ?p ?o. FILTER NOT EXISTS { GRAPH <http://al_ex#g3> { ?s ?p ?o  }  } } }";
        String command_temp_1 = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nDB.DBA.TTLP_MT (file_to_string_output ('"+temp_il_path+"'), '', 'http://al_ex#g3');\nSPARQL "+sparql_temp_valid+";\nEOF";

        StringBuffer temp_output_1 = new StringBuffer();
        String[] cmd_temp_1 = {"/bin/sh" , "-c" , command_temp_1 };
        try {
         Process t1 = Runtime.getRuntime().exec(cmd_temp_1);
         //t1.waitFor();
         BufferedReader reader_temp_1 = new BufferedReader(new InputStreamReader(t1.getInputStream()));
         String line_temp_1 = "";

         while ((line_temp_1 = reader_temp_1.readLine())!= null){
          if( !line_temp_1.equals("") ){
           if(line_temp_1.charAt(0) == 'h' && line_temp_1.charAt(1) == 't' ){
            temp_output_1.append( line_temp_1 + "\n");
            //System.out.println(line_temp_1);
            //count++;
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

        temp_il_file.delete();

        String temp_output_11 = temp_output_1.toString();
        temp_output_11 = temp_output_11.replace(" ", "");
        temp_output_11 = temp_output_11.replace("http://", "> <http://");
        temp_output_11 = temp_output_11.replace("\n", ">.\n");
        temp_output_11 = temp_output_11.replace("\n> <", "\n<");
        temp_output_11 = temp_output_11.replaceFirst("> <" , "<");

        valid_il_writer.write(temp_output_11);
        valid_il_writer.close();

        //////////

        String sparql_temp_invalid = "SELECT ?s ?p ?o WHERE { GRAPH <http://al_ex#g1> {?s ?p ?o. FILTER EXISTS { GRAPH <http://al_ex#g3> { ?s ?p ?o  }  } } }";
        String command_temp_2 = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nSPARQL "+sparql_temp_invalid+";\n SPARQL CLEAR GRAPH <http://al_ex#g3>;\nEOF";

        StringBuffer temp_output_2 = new StringBuffer();
        String[] cmd_temp_2 = {"/bin/sh" , "-c" , command_temp_2 };
        try {
         Process t2 = Runtime.getRuntime().exec(cmd_temp_2);
         //t1.waitFor();
         BufferedReader reader_temp_2 = new BufferedReader(new InputStreamReader(t2.getInputStream()));
         String line_temp_2 = "";

         while ((line_temp_2 = reader_temp_2.readLine())!= null){
          if( !line_temp_2.equals("") ){
           if(line_temp_2.charAt(0) == 'h' && line_temp_2.charAt(1) == 't' ){
            temp_output_2.append( line_temp_2 + "\n");
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

        String temp_output_22 = temp_output_2.toString();
        temp_output_22 = temp_output_22.replace(" ", "");
        temp_output_22 = temp_output_22.replace("http://", "> <http://");
        temp_output_22 = temp_output_22.replace("\n", ">.\n");
        temp_output_22 = temp_output_22.replace("\n> <", "\n<");
        temp_output_22 = temp_output_22.replaceFirst("> <" , "<");

        invalid_il_writer.write(temp_output_22);
        invalid_il_writer.close();

        //////////////////////////////////////////////////////////////////////////


        //Execute command_3_e
        String[] cmd3 = {"/bin/sh" , "-c" , command_3_e };
        try {
         Process p3 = Runtime.getRuntime().exec(cmd3);
         p3.waitFor();
         BufferedReader reader3 = new BufferedReader(new InputStreamReader(p3.getInputStream()));
         String line3 = "";

         while ((line3 = reader3.readLine())!= null){
          output.append(line3 + "\n");
          //System.out.print(line3);
          //count++;
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

       }
       //---------------------------------------------------------------------------------------------------------------------------------------------
       //---------------------------------------------------------------------------------------------------------------------------------------------
       // Executing - where param_3 = "F" or "FG"
       else if(error == false && (param_3.equals("F") || param_3.equals("FG")) ){

        int count = 0;
        String sparql_count_command = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nSPARQL SELECT DISTINCT (count(?s) AS ?count) WHERE { GRAPH <http://al_ex#g1> { ?s ?p ?o } };\nEOF";
        String total = "";

        //Execute command_1
        String[] cmd1 = {"/bin/sh" , "-c" , command_1 };
        try {
         Process p1 = Runtime.getRuntime().exec(cmd1);
         p1.waitFor();
         BufferedReader reader1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
         String line1 = "";

         while ((line1 = reader1.readLine())!= null){
          if( !line1.equals("") ){
           if(line1.charAt(0) == 'h' && line1.charAt(1) == 't' ){
            output.append( line1 + "\n");
            //System.out.println(line1);
            count++;
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

        int it = 1;
        //Execute sparql_count_command
        String[] cmd2 = {"/bin/sh" , "-c" , sparql_count_command };
        try {
         Process p2 = Runtime.getRuntime().exec(cmd2);
         p2.waitFor();
         BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
         String line2 = "";

         while ((line2 = reader2.readLine())!= null){
          if( it == 9){
           total = line2;
           //System.out.println(line2);
          }
          it++;
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

        log_writer.write("Invald interlinks due to DBpedia resources:\n"+output.toString()+"\nNumber of invalid interlinks due to DBpedia resources: "+count+"\n");
        if(count > 0)
         log_writer.write("Invalidation Reason: DBpedia resource identifiers do not exist in DBpedia dataset\n\n");
        else
         log_writer.write("\n\n");


        // RDF Log
        rdf_log_writer.write("_:b"+blank_node+"  a  dlo:InterlinkValidationReport ;\n     dlo:wasProducedBy  _:b"+(blank_node-1)+" ;\n     ");

        if( !output.toString().equals("") ){
         String[] rdf_temp_output_1 = output.toString().split("\n");
         int cnt_tmp1 = rdf_temp_output_1.length;
         String[][] rdf_temp_output_2 = new String[cnt_tmp1][];

         for(int i = 0; i < cnt_tmp1; i++){
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replace(" ", "");
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replace("http://", " http://");
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replaceFirst(" ", "");
          rdf_temp_output_2[i] = rdf_temp_output_1[i].split(" ");

          rdf_log_writer.write("ia:invalidInterlink  [  rdf:subject  <"+rdf_temp_output_2[i][0]+"> ;\n                             rdf:predicate  <"+rdf_temp_output_2[i][1]+"> ;\n                             rdf:object  <"+rdf_temp_output_2[i][2]+"> ] ;\n     ");
         }
        }


        /////////////////////////////////////////////////////////////////////////
        String temp_output = output.toString();

        temp_output = temp_output.replace(" ", "");
        temp_output = temp_output.replace("http://", "> <http://");
        temp_output = temp_output.replace("\n", ">.\n");
        temp_output = temp_output.replace("\n> <", "\n<");
        temp_output = temp_output.replaceFirst("> <" , "<");

        temp_il_writer.write(temp_output);
        //temp_il_writer.close();
        /////////////////////////////////////////////////////////////////////////


        output = new StringBuffer();
        count = 0;


        //Execute command_2
        String[] cmd3 = {"/bin/sh" , "-c" , command_2 };
        try {
         Process p3 = Runtime.getRuntime().exec(cmd3);
         //p3.waitFor();
         BufferedReader reader3 = new BufferedReader(new InputStreamReader(p3.getInputStream()));
         String line3 = "";

         while ((line3 = reader3.readLine())!= null){
          if( !line3.equals("") ){
           if( (line3.charAt(0) == 'h' && line3.charAt(1) == 't') || (line3.charAt(0) == 'I' && line3.charAt(1) == 'n') ){
            if( line3.charAt(0) == 'I' ){
             String temp = "";
             temp = line3.substring(18);
             output.append( temp + "\n");
             //System.out.print(temp);
            }
            else {
             output.append(line3 + "\n");
            }
            count++;
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

        log_writer.write("Invalid interlinks due to "+param_1+" resources:\n"+output.toString()+"\nNumber of invalid "+param_1+" resources: "+count+"\n");
        if(count > 0)
         log_writer.write("Invalidation Reason: "+param_1+" resource identifiers do not exist in "+param_1+" dataset\n");
        log_writer.write("\n-----------------------------------------------------------------------------------------------\n");


        // RDF Log
        if( !output.toString().equals("") ){
         String[] rdf_temp_output_1 = output.toString().split("\n");
         int cnt_tmp1 = rdf_temp_output_1.length;
         String[][] rdf_temp_output_2 = new String[cnt_tmp1][];

         for(int i = 0; i < cnt_tmp1; i++){
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replace(" ", "");
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replace("http://", " http://");
          rdf_temp_output_1[i] = rdf_temp_output_1[i].replaceFirst(" ", "");
          rdf_temp_output_2[i] = rdf_temp_output_1[i].split(" ");

          rdf_log_writer.write("ia:invalidInterlink  [  rdf:subject  <"+rdf_temp_output_2[i][0]+"> ;\n                             rdf:predicate  <"+rdf_temp_output_2[i][1]+"> ;\n                             rdf:object  <"+rdf_temp_output_2[i][2]+"> ] ;\n     ");
         }
        }

        rdf_log_writer.write("prov:generatedAtTime \""+getTimeandDate('f')+"\"^^xsd:dateTime .\n\n\n");

        /////////////////////////////////////////////////////////////////////
        temp_output = "";
        temp_output = output.toString();

        String temp_il_path = executeCommand("pwd")+"/temp_il.nt";

        temp_output = temp_output.replace(" ", "");
        temp_output = temp_output.replace("http://", "> <http://");
        temp_output = temp_output.replace("\n", ">.\n");
        temp_output = temp_output.replace("\n> <", "\n<");
        temp_output = temp_output.replaceFirst("> <" , "<");

        temp_il_writer.write(temp_output);
        temp_il_writer.close();

        String sparql_temp_valid = "SELECT ?s ?p ?o WHERE { GRAPH <http://al_ex#g1> {?s ?p ?o. FILTER NOT EXISTS { GRAPH <http://al_ex#g3> { ?s ?p ?o  }  } } }";
        String command_temp_1 = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nDB.DBA.TTLP_MT (file_to_string_output ('"+temp_il_path+"'), '', 'http://al_ex#g3');\nSPARQL "+sparql_temp_valid+";\nEOF";

        StringBuffer temp_output_1 = new StringBuffer();
        String[] cmd_temp_1 = {"/bin/sh" , "-c" , command_temp_1 };
        try {
         Process t1 = Runtime.getRuntime().exec(cmd_temp_1);
         //t1.waitFor();
         BufferedReader reader_temp_1 = new BufferedReader(new InputStreamReader(t1.getInputStream()));
         String line_temp_1 = "";

         while ((line_temp_1 = reader_temp_1.readLine())!= null){
          if( !line_temp_1.equals("") ){
           if(line_temp_1.charAt(0) == 'h' && line_temp_1.charAt(1) == 't' ){
            temp_output_1.append( line_temp_1 + "\n");
            //System.out.println(line_temp_1);
            //count++;
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

        temp_il_file.delete();

        String temp_output_11 = temp_output_1.toString();
        temp_output_11 = temp_output_11.replace(" ", "");
        temp_output_11 = temp_output_11.replace("http://", "> <http://");
        temp_output_11 = temp_output_11.replace("\n", ">.\n");
        temp_output_11 = temp_output_11.replace("\n> <", "\n<");
        temp_output_11 = temp_output_11.replaceFirst("> <" , "<");

        valid_il_writer.write(temp_output_11);
        valid_il_writer.close();

        //////////

        String sparql_temp_invalid = "SELECT ?s ?p ?o WHERE { GRAPH <http://al_ex#g1> {?s ?p ?o. FILTER EXISTS { GRAPH <http://al_ex#g3> { ?s ?p ?o  }  } } }";
        String command_temp_2 = path_to_isql+" 1111 dba "+dba_password+" <<'EOF'\nSPARQL "+sparql_temp_invalid+";\n SPARQL CLEAR GRAPH <http://al_ex#g3>;\nEOF";

        StringBuffer temp_output_2 = new StringBuffer();
        String[] cmd_temp_2 = {"/bin/sh" , "-c" , command_temp_2 };
        try {
         Process t2 = Runtime.getRuntime().exec(cmd_temp_2);
         //t1.waitFor();
         BufferedReader reader_temp_2 = new BufferedReader(new InputStreamReader(t2.getInputStream()));
         String line_temp_2 = "";

         while ((line_temp_2 = reader_temp_2.readLine())!= null){
          if( !line_temp_2.equals("") ){
           if(line_temp_2.charAt(0) == 'h' && line_temp_2.charAt(1) == 't' ){
            temp_output_2.append( line_temp_2 + "\n");
           }
          }
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

        String temp_output_22 = temp_output_2.toString();
        temp_output_22 = temp_output_22.replace(" ", "");
        temp_output_22 = temp_output_22.replace("http://", "> <http://");
        temp_output_22 = temp_output_22.replace("\n", ">.\n");
        temp_output_22 = temp_output_22.replace("\n> <", "\n<");
        temp_output_22 = temp_output_22.replaceFirst("> <" , "<");

        invalid_il_writer.write(temp_output_22);
        invalid_il_writer.close();

        //////////////////////////////////////////////////////////////////////////

        //Execute command_3_e
        String[] cmd4 = {"/bin/sh" , "-c" , command_3_e };
        try {
         Process p4 = Runtime.getRuntime().exec(cmd4);
         p4.waitFor();
         BufferedReader reader4 = new BufferedReader(new InputStreamReader(p4.getInputStream()));
         String line4 = "";

         while ((line4 = reader4.readLine())!= null){
          output.append(line4 + "\n");
          //System.out.print(line4);
          //count++;
         }
        }
        catch (Exception e){
         e.printStackTrace();
        }

       }

       // Clean up temp downloaded files
       if(param_2.charAt(0) == 'h' && param_2.charAt(1) == 't'){
        try {
         File al_temp_1111 = new File(param_2_temp);
         al_temp_1111.delete();
        }
        catch (Exception e){
         e.printStackTrace();
        }
       }

       if(param_3.equals("D")){
        if(param_4.charAt(0) == 'h' && param_4.charAt(1) == 't'){
         try {
          File al_temp_2222 = new File(param_4_temp);
          al_temp_2222.delete();
         }
         catch (Exception e){
          e.printStackTrace();
         }
        }
       }

       //---------------------------------------------------------------------------------------------------------------------------------------------

      }
     }
    }

    ////////////////////////////////////

   }

   log_writer.close();
   rdf_log_writer.close();
   in.close();
  }

  catch (Exception e){
   System.err.println("ERROR: " + e.getMessage());
  }


  ///////////////////

 }

}

