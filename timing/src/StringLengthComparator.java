import java.util.Comparator;

class StringLengthComparator implements Comparator<String> {
    public int compare(String o1, String o2) {
    	int ret = 0;
        int l1 = o1.length();
        int l2 = o2.length();
        
        if (l1 > l2)
        	ret = 1;
        else if (l2 > l1)
        	ret = -1;
        
        return ret;
    }
}
