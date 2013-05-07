package Util
{
	 import flash.utils.Dictionary;

    /**
     * IMap implementation which dynamically creates a HashMap of 
     * key and value pairs which provides a standard API for
     * working with the map
     */
    dynamic public class HashMap extends Dictionary implements IMap
    {
        /**
         * Constructor
         * 
         * <p>
         * Creates a new HashMap instance. By default, weak key 
         * references are used in order to ensure that objects are 
         * eligible for Garbage Collection when no longer being
         * referenced
         * </p>
         * 
         * @param specifies if weak key references should be used
         */        
        public function HashMap(useWeakReferences:Boolean = true)
        {
            super( useWeakReferences );
        }
        
        /**
         * Adds a key and value to the current HashMap
         * 
         * @param the key to add to the map
         * @param the value of the specified key
         */
        public function put(key:String, value:*) : void
        {
            this[key] = value;    
        }

        /**
         * Removes a key and value from the current Map
         * 
         * @param the key to remove from the map
         */
        public function remove(key:String) : void
        {
            this[key] = null;
            delete this[key];
        }
        
        /**
         * Determines if a key exists in the current map
         * 
         * @param  the key in which to determine existance in the map
         * @return true if the key exisits, false if not
         */
        public function containsKey(key:String) : Boolean
        {
            return this[key] != null
        }
        
        /**
         * Determines if a value exists in the current map
         * 
         * @param  the value in which to determine existance in the map
         * @return true if the value exisits, false if not
         */
        public function containsValue(value:*) : Boolean
        {
            for (var prop:String in this) {
                
                if (this[prop] == value)
                {
                    return true;
                }
            }
            return false;
        }
        
        /**
         * Returns a key value from the current Map
         * 
         * @param  the key in which to retrieve the value of
         * @return the value of the specified key
         */
        public function getKey(value:*) : String
        {
            for (var prop:String in this) {
                
                if (this[prop] == value)
                {
                    return prop;
                }
            }
            return null;
        }
        
        /**
         * Returns a key value from the current Map
         * 
         * @param  the key in which to retrieve the value of
         * @return the value of the specified key
         */
        public function getValue(key:String) : *
        {
            return this[key];
        }
                
        /**
         * Returns the size of this map
         * 
         * @return the current size of the map instance
         */
        public function size()  : int
        {
            var size:int = 0;
            
            for (var prop:String in this) {
                size++;        
            }
            return size;
        }
        
        /**
         * Determines if the current map is empty
         * 
         * @return true if the current map is empty, false if not
         */
        public function isEmpty() : Boolean
        {
            return this.size() <= 0;
        }
        
        /**
         * Resets all key / values in map to null
         */
        public function clear() : void
        {
            for (var prop:String in this) {    
                        
                this[prop] = null;
                delete this[prop];
            }
        }
    }
}