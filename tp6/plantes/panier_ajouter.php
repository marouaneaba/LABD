<?php
  $local_panier="panier_local.xml";

  if (fopen($local_panier,"r")==NULL) {
    error_log("creating empty basket") ;
    include "panier_vider.php";
  }


  $plant=$_REQUEST['plant'];
  $cmd="/usr/bin/xsltproc --stringparam plant \"$plant\" panier_ajouter.xsl $local_panier > /tmp/panier.xml";
  $cmd.="; mv /tmp/panier.xml ".$local_panier;
  error_log($cmd);
  passthru($cmd);
  echo "<meta http-equiv='refresh' content='0; panier.php' />";

  ?>
