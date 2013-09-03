import java.lang.*;
import java.io.*;
class assignmentA{
	private int m;
	private int n;
	private double l;
	private double h;
	private double beam[] = new double[3];
	private double col[] = new double[3];
	private double kts[][];
	private double P[];
	
	assignmentA(int mm, int nn, double ll, double hh, double bbeam[], double ccol[] ){
		m=mm;
		n=nn;
		l=ll;
		h=hh;
		for (int i=0;i<3;i++){
			beam[i]=bbeam[i];
		}
		for (int i=0;i<3;i++){
			col[i]=ccol[i];
		}
	}
	
	public void flush(){
		kts=new double[3*m*n][3*m*n];
		P=new double[3*m*n];
		for (int i=0;i<3*m*n;i++){
			for (int j=0;j<3*m*n;j++){
				kts[i][j]=0.0;
			}
		}
		for (int i=0;i<3*m*n;i++){
			P[i]=0.0;
		}
	}
	
	public int matrixSize(double arr[][]){
		int c=1;
		try{
			while(c>0){
				arr[c-1][0]*=1;
				c++;
			}
		}
		catch (ArrayIndexOutOfBoundsException e){
			c=c-1;
		}
		return c;
	}
	
	public double[][] matrixComplete(double Kg[][]){
		int size=matrixSize(Kg);
		for (int i=0;i<size;i++){
			for (int j=0;j<=i;j++){
				if (i!=j)
					Kg[j][i]=Kg[i][j];
			}
		}
		return Kg;
	}
	
	//make 3*3
	public double[][] verticalT(){
		double temp[][]=new double[6][6];
		for (int i=0;i<6;i++){
			for (int j=0;j<6;j++){
				temp[i][j]=0;
			}
		}
		temp[2][2]=1;
		temp[5][5]=1;
		temp[0][1]=1;
		temp[3][4]=1;
		temp[1][0]=-1;
		temp[4][3]=-1;
		return temp;
	}
	
	public double[][] transpose(double arr[][]){
		int size=matrixSize(arr);
		double trans[][]=new double[size][size];
		for (int i=0;i<size;i++){
			for (int j=0;j<size;j++){
				trans[j][i]=arr[i][j];
			}
		}
		return trans;
	}
	
	public double[][] multiply(double a[][], double b[][]){
		int size=matrixSize(a);
		double temp[][]=new double[size][size];
		for (int i=0;i<size;i++){
			for (int j=0;j<size;j++){
				for (int k=0;k<size;k++)
					temp[i][j]+=a[i][k]*b[k][j];
			}
		}
		return temp;
	}
	
	public void localH (int x, double w){
		double Kg[][]=new double[6][6];
		for (int i=0;i<6;i++){
			for (int j=0;j<6;j++){
				Kg[i][j]=0;
			}
		}
		Kg[0][0]=beam[2]*beam[0]/l;
		Kg[3][3]=Kg[0][0];
		Kg[1][1]=12.0*beam[2]*beam[1]/(l*l*l);
		Kg[4][4]=Kg[1][1];
		Kg[2][1]=6.0*beam[2]*beam[1]/(l*l);
		Kg[5][1]=Kg[2][1];
		Kg[2][2]=4.0*beam[2]*beam[1]/l;
		Kg[5][5]=Kg[2][2];
		Kg[3][0]=-1.0*Kg[0][0];
		Kg[4][1]=-1.0*12.0*beam[2]*beam[1]/(l*l*l);
		Kg[4][2]=-1.0*6.0*beam[2]*beam[1]/(l*l);
		Kg[5][4]=Kg[4][2];
		Kg[5][2]=2.0*beam[2]*beam[1]/l;
		
		double KG[][]=matrixComplete(Kg);
		
		//********** when m<n ************
		
		if (m<n){
			String ppp=""+x+"-"+(x+1)+".txt";
			try{//printing 
				BufferedWriter bw = new BufferedWriter(new FileWriter(ppp));
				for (int i=0;i<6;i++){
					for (int j=0;j<6;j++){
						bw.write(""+KG[i][j]+"\t");
					}
					bw.newLine();//
				}
			bw.close();
			}
			catch(Exception e){}
			
			for (int i=0;i<6;i++){
				for (int j=0;j<6;j++){
					double temp = KG[i][j];
					if ( i<3 && j <3 )
						kts[3*x+(i%3-3)][3*x+(j%3-3)]+=temp;
					else if ( i<3 && j >=3 )
						kts[3*x+(i%3-3)][3*(x+1)+(j%3-3)]+=temp;
					else if ( i>=3 && j <3 )
						kts[3*(x+1)+(i%3-3)][3*x+(j%3-3)]+=temp;
					else if ( i>=3 && j >=3 )
						kts[3*(x+1)+(i%3-3)][3*(x+1)+(j%3-3)]+=temp;
				}
			}
			P[3*x-3]=0.0;
			P[3*(x+1)-3]=0.0;
			P[3*x-2]=-w*l/2;
			P[3*(x+1)-2]=-w*l/2;
			P[3*x-1]=-w*l*l/12;
			P[3*(x+1)-1]=w*l*l/12;
		}		
		
		//************ when n>m ************
		
		else{
			String ppp=""+x+"-"+(x+n-1)+".txt";
			try{//printing 
				BufferedWriter bw = new BufferedWriter(new FileWriter(ppp));
				for (int i=0;i<6;i++){
					for (int j=0;j<6;j++){
						bw.write(""+KG[i][j]+"\t");
					}
					bw.newLine();//
				}
			bw.close();
			}
			catch(Exception e){}
			
			for (int i=0;i<6;i++){
				for (int j=0;j<6;j++){
					double temp = KG[i][j];
					if ( i<3 && j <3 )
						kts[3*x+(i%3-3)][3*x+(j%3-3)]+=temp;
					else if ( i<3 && j >=3 )
						kts[3*x+(i%3-3)][3*(x+n-1)+(j%3-3)]+=temp;
					else if ( i>=3 && j <3 )
						kts[3*(x+n-1)+(i%3-3)][3*x+(j%3-3)]+=temp;
					else if ( i>=3 && j >=3 )
						kts[3*(x+n-1)+(i%3-3)][3*(x+n-1)+(j%3-3)]+=temp;
				}
			}
			
			P[3*x-3]=0.0;
			P[3*(n+x-1)-3]=0.0;
			P[3*x-2]=-w*l/2;
			P[3*(n+x-1)-2]=-w*l/2;
			P[3*x-1]=-w*l*l/12;
			P[3*(n+x-1)-1]=w*l*l/12;
		}	
	}
	
	public void localV(int x){
		double Kg[][]=new double[6][6];
		for (int i=0;i<6;i++){
			for (int j=0;j<6;j++){
				Kg[i][j]=0;
			}
		}
		Kg[0][0]=beam[2]*beam[0]/h;
		Kg[3][3]=Kg[0][0];
		Kg[1][1]=12.0*beam[2]*beam[1]/(h*h*h);
		Kg[4][4]=Kg[1][1];
		Kg[2][1]=6.0*beam[2]*beam[1]/(h*h);
		Kg[5][1]=Kg[2][1];
		Kg[2][2]=4.0*beam[2]*beam[1]/h;
		Kg[5][5]=Kg[2][2];
		Kg[3][0]=-1.0*Kg[0][0];
		Kg[4][1]=-1.0*12.0*beam[2]*beam[1]/(h*h*h);
		Kg[4][2]=-1.0*6.0*beam[2]*beam[1]/(h*h);
		Kg[5][4]=Kg[4][2];
		Kg[5][2]=2.0*beam[2]*beam[1]/h;
		
		double K[][]=matrixComplete(Kg);
		double T[][]=verticalT();
		double TT[][]=transpose(T);
		double KGTemp[][]=multiply(TT,K);
		double KG[][]=multiply(KGTemp,T);
		
		//****** when m<n *******
		
		if (m<n){
			String ppp="";		
			ppp=""+x+"-"+(x+m)+".txt";
			try{//printing 
				BufferedWriter bw = new BufferedWriter(new FileWriter(ppp));
				for (int i=0;i<6;i++){
					for (int j=0;j<6;j++){
						bw.write(""+KG[i][j]+"\t");
					}
					bw.newLine();
				}
			bw.close();
			}
			catch(Exception e){}
			
			for (int i=0;i<6;i++){
				for (int j=0;j<6;j++){
					double temp = KG[i][j];
					if ( i<3 && j <3 )
						kts[3*(x+m)+(i%3-3)][3*(x+m)+(j%3-3)]+=temp;
					else if ( i<3 && j >=3 )
						kts[3*(x+m)+(i%3-3)][3*x+(j%3-3)]+=temp;
					else if ( i>=3 && j <3 )
						kts[3*x+(i%3-3)][3*(x+m)+(j%3-3)]+=temp;
					else if ( i>=3 && j >=3 )
						kts[3*x+(i%3-3)][3*x+(j%3-3)]+=temp;
				}
			}
		}	
		
		//******v when n>m ******
		
		else{
			String ppp="";		
			if (x%(n-1)==0)
				ppp=""+x+"-"+( (x/(n-1))+(n-1)*(m) )+".txt";
			else
				ppp=""+x+"-"+(x+1)+".txt";
			try{//printing 
				BufferedWriter bw = new BufferedWriter(new FileWriter(ppp));
				for (int i=0;i<6;i++){
					for (int j=0;j<6;j++){
						bw.write(""+KG[i][j]+"\t");
					}
					bw.newLine();
				}
			bw.close();
			}
			catch(Exception e){}
			
			if (x%(n-1)==0){
				for (int i=0;i<6;i++){
					for (int j=0;j<6;j++){
						double temp = KG[i][j];
						if ( i<3 && j <3 )
							kts[3*((x/(n-1))+(n-1)*(m))+(i%3-3)][3*((x/(n-1))+(n-1)*(m))+(j%3-3)]+=temp;
						else if ( i<3 && j >=3 )
							kts[3*((x/(n-1))+(n-1)*(m))+(i%3-3)][3*x+(j%3-3)]+=temp;
						else if ( i>=3 && j <3 )
							kts[3*x+(i%3-3)][3*((x/(n-1))+(n-1)*(m))+(j%3-3)]+=temp;
						else if ( i>=3 && j >=3 )
							kts[3*x+(i%3-3)][3*x+(j%3-3)]+=temp;
						
					}
				}
			}	
			
			else{
				for (int i=0;i<6;i++){
					for (int j=0;j<6;j++){
						double temp = KG[i][j];
						if ( i<3 && j <3 )
							kts[3*(x+1)+(i%3-3)][3*(x+1)+(j%3-3)]+=temp;
						else if ( i<3 && j >=3 )
							kts[3*(x+1)+(i%3-3)][3*x+(j%3-3)]+=temp;
						else if ( i>=3 && j <3 )
							kts[3*x+(i%3-3)][3*(x+1)+(j%3-3)]+=temp;
						else if ( i>=3 && j >=3 )
							kts[3*x+(i%3-3)][3*x+(j%3-3)]+=temp;
					}
				}
			}
		}		
	}
	
	public double[][] KTSMatrix(){
		return kts;
	}
	
	public double[] PMatrix(){
		double temp[]=new double[3*(m*n-m)];
		for (int i=0;i<3*(m*n-m);i++)
			temp[i]=P[i];
		return P;
	}
	
	public double[][] computeKppMatrix(double arr[][]){
		double temp[][]=new double[3*(m*n-m)][3*(m*n-m)];
		for (int i=0;i<3*(m*n-m);i++){
			for (int j=0;j<3*(m*n-m);j++){
				temp[i][j]=arr[i][j];
			}
		}
		return temp;
	}
	
	public double[][] computeKxpMatrix(double arr[][]){
		double temp[][]=new double[3*m][3*(m*n-m)];
		for (int i=3*(m*n-m);i<3*m*n;i++){
			for (int j=0;j<3*(m*n-m);j++){
				temp[i-3*(m*n-m)][j]=arr[i][j];
			}
		}
		return temp;
	}
	
	public double[][] computeVMatrix(double arr[][]){
		int size=matrixSize(arr);
		double temp[][]=new double[size][size];
		temp[0][0]=Math.sqrt(arr[0][0]);
		for (int i=1;i<size;i++)
			temp[0][i]=arr[0][i]/temp[0][0];		
		
		for (int i=1;i<size;i++){
			for (int j=0;j<size;j++){
				if (i==j){
					double sum=0;
					for (int k=0;k<j;k++)
						sum+=temp[k][j]*temp[k][j];
					temp[j][j]=Math.sqrt(arr[j][j]-sum);
				}
				else if (j>i){
					double sum=0;
					for (int k=0;k<i;k++)
						sum+=temp[k][i]*temp[k][j];
					temp[i][j]=(arr[i][j]-sum)/temp[i][i];
				}
				else
					temp[i][j]=0;
			}
		}
		return temp;
	}
	
	public double[] computeMatrix(double arr[][], double a[], int ch){
		double temp[]=new double[3*(m*n-m)];
		for (int i=0;i<3*(m*n-m);i++){
			temp[i]=0.0;
		}
		switch (ch){
			case 1:
			for (int i=0;i<3*(m*n-m);i++){
				double s=0.0;
				for (int j=0;j<i;j++)
					s+=arr[i][j]*temp[j];
				temp[i]=(a[i]-s)/arr[i][i];
			}
			return temp;
			case 2:
			for (int i=3*(m*n-m)-1;i>=0;i--){
				double s=0.0;
				for (int j=3*(m*n-m)-1;j>i;j--)
					s+=arr[i][j]*temp[j];
				temp[i]=(a[i]-s)/arr[i][i];
			}
			return temp;
		}
		return temp;
	}
	
	public double[] computeX(double Kxp[][], double U[]){
		double X[]=new double[3*m];
		for (int i=0;i<3*m;i++){
			X[i]=0.0;
		}
		for (int i=0;i<3*m;i++){
			double s=0.0;
			for (int j=0;j<3*(m*n-m);j++){
				s+=Kxp[i][j]*U[j];
			}
			X[i]=s;
		}
		return X;
	}
		
}	
