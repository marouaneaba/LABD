let $bib := "biblio.xml"
return
<results> 
   { 
   for $b in doc($bib)//book, 
       $t in $b/title, 
       $a in $b/author 
   return 
      <result> 
         {$t,$a} 
      </result> 
} 
</results>
