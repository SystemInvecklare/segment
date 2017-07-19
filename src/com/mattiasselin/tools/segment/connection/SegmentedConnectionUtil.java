package com.mattiasselin.tools.segment.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class SegmentedConnectionUtil {
	private static final byte ONE_BYTE = 0b01111111;
	private static final byte TWO_BYTE = 0b00011111;
	private static final byte THREE_BYTE = 0b00001111;
	private static final byte FOUR_BYTE = 0b00000111;
	private static final byte FELLOW_BYTE = 0b00111111;
	
	private static final int FELLOW_BYTE_PREFIX = 0b10000000;
	private static final int TWO_BYTE_PREFIX = 0b11000000;
	private static final int THREE_BYTE_PREFIX = 0b11100000;
	private static final int FOUR_BYTE_PREFIX = 0b11110000;

	public static byte[] readSegment(InputStream inputStream) throws IOException {
		int segmentLength = readSegmentLength(inputStream);
		byte[] segment = new byte[segmentLength];
		
		int read = -1;
		int offset = 0;
		while(offset < segmentLength && (read = inputStream.read(segment, offset, segmentLength-offset)) != -1)
		{
			offset += read;
		}
		return segment;
	}
	
	private static int readSegmentLength(InputStream inputStream) throws IOException
	{
		int readInteger = inputStream.read();
		if(readInteger == -1)
		{
			throw new IOException("End of stream.");
		}
		byte b1 = (byte) readInteger;
		if(getFirstBit(b1))
		{
			if(!getBit(b1, 2)) //second bit
			{
				throw new IOException("Expexted 110xxxxx, 1110xxxx or 11110xxx.");
			}
			byte b2 = (byte) inputStream.read();
			if(getBit(b1, 3)) //1110xxxx or 11110xxx
			{
				byte b3 = (byte) inputStream.read();
				if(getBit(b1, 4)) //11110xxx
				{
					//11110xxx --> Four bytes
					byte b4 = (byte) inputStream.read();
					return decompress(b1,b2,b3,b4);
				}
				else
				{
					//1110xxxx --> Three bytes
					return decompress(b1,b2,b3);
				}
			}
			else
			{
				//110xxxxx --> two bytes.
				return decompress(b1,b2);
			}
		}
		else
		{
			//Single byte.
			return decompress(b1);
		}
	}
	
	
	public static void writeSegment(OutputStream out, byte[] bytes) throws IOException {
		out.write(compress(bytes.length));
		out.write(bytes);
	}
	
	private static int decompress(byte ... data)
	{
		switch (data.length) {
		case 1:
			return get(data[0], ONE_BYTE);
		case 2:
			return (get(data[0], TWO_BYTE) << 6) + get(data[1], FELLOW_BYTE);
		case 3:
			return (((get(data[0], THREE_BYTE) << 6) + get(data[1], FELLOW_BYTE)) << 6)+get(data[2], FELLOW_BYTE);
		case 4:
			return (((((get(data[0], FOUR_BYTE) << 6) + get(data[1], FELLOW_BYTE)) << 6)+get(data[2], FELLOW_BYTE)) << 6)+get(data[3], FELLOW_BYTE);
		default:
			throw new IllegalArgumentException("Illegal length "+data.length);
		}
	}
	
	private static byte[] compress(int value)
	{
		if(value < 0)
		{
			throw new IllegalArgumentException(""+value);
		}
		if(value < 128)
		{
			return new byte[]{(byte) value};
		}
		else
		{
			byte red = (byte) ((value & FELLOW_BYTE) | FELLOW_BYTE_PREFIX);
			value = value >> 6;
			if(value < 32)
			{
				byte green = (byte) ((value & TWO_BYTE) | TWO_BYTE_PREFIX);
				return new byte[]{green,red};
			}
			else
			{
				byte green = (byte) ((value & FELLOW_BYTE) | FELLOW_BYTE_PREFIX);
				value = value >> 6;
				if(value < 16)
				{
					byte blue = (byte) ((value & THREE_BYTE) | THREE_BYTE_PREFIX);
					return new byte[]{blue, green, red};
				}
				else
				{
					byte blue = (byte) ((value & FELLOW_BYTE) | FELLOW_BYTE_PREFIX);
					value = value >> 6;
					if(value < 8)
					{
						byte purple = (byte) ((value & FOUR_BYTE) | FOUR_BYTE_PREFIX);
						return new byte[]{purple, blue, green, red};
					}
					else
					{
						throw new IllegalArgumentException("Too big! (max 2 097 151)");
					}
				}
			}
		}
	}
	
	private static byte get(byte from, byte mask)
	{
		return (byte) (from & mask);
	}
	
	private static boolean getFirstBit(byte b)
	{
	   return getBit(b, 1);
	}
	
	private static boolean getBit(byte b, int position)
	{
	   return ((b >> (8-position)) & 1) == 1;
	}
}
