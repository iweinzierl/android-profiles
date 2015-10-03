package de.iweinzierl.easyprofiles.persistence;

import android.util.Log;

import com.orm.entity.annotation.PrePersist;

public class GeneralListener {

   @PrePersist
   public void prePersist(Object object) {
      Log.d("easyprofiles", "PRE PERSIST: " + object);
   }
}
