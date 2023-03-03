
public class PQueue{

	TreeNode head,
			tail;
	int size = 0;
	
	public void push(TreeNode node)
	{
		TreeNode start = head;													//we assign a new node, start equal to head
		
		if (head == null)
        {
            head = node;
            tail = head;
        }
		
		else if (node.frequency < head.frequency)						//if the new node is less frequent then the head node, the next node will become the head and the head will become the new node
			{
				node.next = head;
				head = node;
			}
		else
		{
		while (start.next != null && start.next.frequency < node.frequency)	//while the node continues and the start node is less than the new node, start node sets itself to its next node
		{
			start = start.next;
		}
		node.next = start.next;
		start.next = node;
		}
		size++;
	}
	
	public TreeNode peek()													//peek will return the head of the priority queue
	{
		return head;
	}
	
	public void poll()														//poll will remove the head of the priority queue by moving everything up by 1
	{
		head = head.next;
		size--;
	}
	
	public String  toString()												//toString will put and return our result in a string
	  {
		  String result = "";
		  TreeNode n = head;  
		  while (n != null)  
			 {  
				 result += n.c + " " + n.frequency + "\n";
				 n = n.next;  
			 }  
		  return result;
	  }
}